package de.morrien.nekeys.voice;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.jsgf.JSGFGrammar;
import edu.cmu.sphinx.jsgf.JSGFGrammarException;
import edu.cmu.sphinx.jsgf.JSGFGrammarParseException;
import edu.cmu.sphinx.jsgf.JSGFRuleGrammar;
import edu.cmu.sphinx.jsgf.parser.JSGFParser;
import edu.cmu.sphinx.jsgf.rule.JSGFRule;
import edu.cmu.sphinx.linguist.dictionary.TextDictionary;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import static de.morrien.nekeys.NotEnoughKeys.logger;

public class SpeechRecognizer implements Runnable {

    boolean recording = false;
    private Microphone microphone;
    private ConfigurationManager cm;
    private Recognizer recognizer;
    private TextDictionary dictionary;
    private JSGFGrammar grammar;
    private JSGFRuleGrammar ruleGrammar;
    private volatile Thread recognitionThread = null;
    private boolean recognitionThreadEnabled = false;
    // A queue of the recognized strings.
    private LinkedList<String> recognizedStringQueue;

    public SpeechRecognizer() {
        try {
            URL configURL = getClass().getResource("/assets/nekeys/voice/config.xml");
            cm = new ConfigurationManager(configURL);

            recognizer = cm.lookup("recognizer");
            microphone = cm.lookup("microphone");
            dictionary = cm.lookup("dictionary");
            grammar = cm.lookup("jsgfGrammar");
            grammar.allocate();
            ruleGrammar = grammar.getRuleGrammar();

            recognizedStringQueue = new LinkedList<>();
        } catch (IOException e) {
            logger.error("Cannot load speech recognizer: ");
            logger.error(e.toString());
        } catch (PropertyException e) {
            logger.error("Cannot configure speech recognizer: ");
            logger.error(e.toString());
        }
    }

    public boolean isWord(String word) {
        return dictionary.getWord(word) != null;
    }

    public void setRule(String ruleName, String ruleContent) {
        if (ruleName == null || ruleContent == null || ruleName.isEmpty() || ruleContent.isEmpty()) return;
        boolean enabled = isEnabled();
        try {
            if (enabled) {
                setEnabled(false);
            }

            JSGFRule rule = JSGFParser.ruleForJSGF(ruleContent);
            if (rule == null) {
                rule = JSGFParser.ruleForJSGF("");
                logger.warn("Could not bind malformed rule \"" + ruleContent + "\"");
            }
            rule.ruleName = ruleName;
            ruleGrammar.setRule(ruleName, rule, true);
            ruleGrammar.setEnabled(true);
            grammar.commitChanges();

            if (enabled) {
                setEnabled(true);
            }
        } catch (IOException e) {
            logger.warn("Cannot set grammar rule");
            logger.warn(e.toString());
        } catch (JSGFGrammarParseException | JSGFGrammarException e) {
            e.printStackTrace();
        }
    }

    public void removeAllRules() {
        boolean enabled = isEnabled();
        if (enabled) setEnabled(false);

        String[] ruleNames = ruleGrammar.getRuleNames().toArray(new String[0]);
        for (String ruleName : ruleNames) {
            ruleGrammar.deleteRule(ruleName);
        }
        ruleGrammar.setEnabled(true);
        try {
            grammar.commitChanges();
        } catch (IOException e) {
            logger.warn("Cannot remove grammar rules");
            logger.warn(e.toString());
        } catch (JSGFGrammarParseException | JSGFGrammarException e) {
            e.printStackTrace();
        }

        if (enabled) setEnabled(true);
    }

    public void removeRule(String ruleName) {
        boolean enabled = isEnabled();
        if (enabled) setEnabled(false);

        ruleGrammar.deleteRule(ruleName);
        ruleGrammar.setEnabled(true);
        try {
            grammar.commitChanges();
        } catch (IOException e) {
            logger.warn("Cannot remove grammar rule");
            logger.warn(e.toString());
        } catch (JSGFGrammarParseException | JSGFGrammarException e) {
            e.printStackTrace();
        }

        if (enabled) setEnabled(true);
    }

    // Contains the main processing to be done by the recognition thread.
    public void run() {
        logger.debug("Recognition thread starting");

        while (recognitionThreadEnabled) {
            Result result = recognizer.recognize();

            if (recording && result != null) {
                String s = result.getBestFinalResultNoFiller();

                // Only save non-empty strings.
                if (!s.equals("")) {
                    recognizedStringQueue.addLast(s);
                }
            }
        }
        logger.debug("Recognition thread finished");
    }

    // Returns the number of recognized strings currently in the
    // recognized string queue.
    public int getQueueSize() {
        return recognizedStringQueue.size();
    }

    // Returns and removes the oldest recognized string from the
    // recognized string queue.  Returns an empty string if the
    // queue is empty.
    public String popString() {
        if (getQueueSize() > 0) {
            return recognizedStringQueue.removeFirst();
        } else {
            return "";
        }
    }

    /**
     * @return true if the microphone is currently enabled.
     */
    public boolean isEnabled() {
        return microphone.isRecording();
    }

    // Enables and disables the speech recognizer.  Starts and stops the
    // speech recognition thread.
    public void setEnabled(boolean enabled) {
        if (enabled) {
            recognizer.allocate();
            logger.debug("Starting microphone...");
            boolean success = microphone.startRecording();
            logger.debug("Microphone on");

            if (!success) {
                logger.warn("Cannot initialize microphone. " +
                        "Speech recognition disabled.");
                return;
            } else {
                if (null != recognitionThread) {
                    logger.warn("New recognition thread being "
                            + "created before the previous one finished.");
                }

                recognitionThread = new Thread(this, "Recognition thread");

                // Start running the recognition thread.
                recognitionThreadEnabled = true;
                recognitionThread.start();
            }
        } else {
            logger.debug("Stopping microphone...");
            microphone.stopRecording();
            logger.debug("Microphone off");

            // The following line indirectly stops the recognition thread
            // from running.  The next time the recognition thread checks
            // this variable, it will stop running.
            recognitionThreadEnabled = false;

            // Wait for the thread to die before proceeding.
            while (recognitionThread != null && recognitionThread.isAlive()) {
                logger.debug("Waiting for recognition thread to die...");

                try {
                    // Have the main thread sleep for a bit...
                    Thread.sleep(10);
                } catch (InterruptedException exception) {
                }
            }

            recognitionThread = null;
            microphone.clear();
            recognizer.deallocate();

            logger.debug("Clearing recognized string queue");
            recognizedStringQueue.clear();
        }
    }

    /**
     * Deallocates speech recognizer.
     */
    public void destroy() {
        // This function call will shut down everything, including the
        // recognition thread.
        setEnabled(false);

        // It should now be safe to deallocate the recognizer.
        recognizer.deallocate();
    }
}
