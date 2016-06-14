package hr.fer.zemris.app.plugins;

import hr.fer.zemris.app.ClipboardStack;
import hr.fer.zemris.app.TextEditorModel;
import hr.fer.zemris.app.UndoManager;
import hr.fer.zemris.app.actions.model_actions.IEditAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mmatak on 6/5/16.
 * //TODO: Fix that when undo, only changed letters go to lowercase, not every first letter.
 * //Must remember before implementation: If I remember whole model, it costs me too much memory.
 * //TODO: Fix number of spaces between words
 */
public class UpperCaseFirstLetter implements IPlugin {
    @Override
    public String getName() {
        return "UpperCase";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        IEditAction action = new IEditAction() {
            @Override
            public void executeDo() {
                ArrayList<String> lines = new ArrayList<>(model.getLines().stream()
                        .map(s -> Arrays.asList(s.split("\\s+"))
                                .stream().map(s1 -> {
                                    if (s1.length() > 1) {
                                        return s1.substring(0, 1).toUpperCase() + s1.substring(1);
                                    } else {
                                        return s1.toUpperCase();
                                    }
                                })
                                .collect(Collectors.joining(" "))
                        ).collect(Collectors.toList())
                );
                model.setLines(lines);
            }

            @Override
            public void executeUndo() {
                ArrayList<String> lines = new ArrayList<>(model.getLines().stream()
                        .map(s -> Arrays.asList(s.split("\\s+"))
                                .stream().map(s1 -> {
                                    if (s1.length() > 1) {
                                        return s1.substring(0, 1).toLowerCase() + s1.substring(1);
                                    } else {
                                        return s1.toLowerCase();
                                    }
                                })
                                .collect(Collectors.joining(" "))
                        ).collect(Collectors.toList())
                );
                model.setLines(lines);
            }
        };
        action.executeDo();
        undoManager.push(action);
    }

}
