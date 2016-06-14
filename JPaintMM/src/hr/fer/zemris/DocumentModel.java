package hr.fer.zemris;

import hr.fer.zemris.shapes.GraphicalObject;
import hr.fer.zemris.util.Point;

import java.util.*;

/**
 * Document model which knows info about objects, how they are stored,
 * which of them are selected,  sends information about them to listeners etc.
 * Created by mmatak on 6/11/16.
 */
public class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10;

    /**
     * Collection of all graphical objects
     */
    private List<GraphicalObject> objects = new ArrayList<>();
    /**
     * Read-Only proxy arround collection of all graphical objects
     */
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    /**
     * Collection of observers
     */
    private List<DocumentModelListener> listeners = new ArrayList<>();
    /**
     * Collection of selected objects.
     */
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    /**
     * Read only proxy around selected objects.
     */
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    /**
     * Listener which is registered on all objects of paint
     */
    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {

        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if (go.isSelected() && !selectedObjects.contains(go)) {
                selectedObjects.add(go);
                notifyListeners();
            }
            if (!go.isSelected() && selectedObjects.contains(go)) {
                selectedObjects.remove(go);
                notifyListeners();
            }
        }
    };


    /**
     * Deletes all objects from model and notifies model observers
     */
    public void clear() {
        for (GraphicalObject go : objects) {
            go.removeGraphicalObjectListener(goListener);
        }
        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    /**
     * Add object in document and registers this model as observer
     */
    public void addGraphicalObject(GraphicalObject obj) {
        objects.add(obj);
        if (obj.isSelected()) {
            selectedObjects.add(obj);
        }
        obj.addGraphicalObjectListener(goListener);
        notifyListeners();
    }

    /**
     * Removes object in document and deregisters this model as observer
     */
    public void removeGraphicalObject(GraphicalObject obj) {
        obj.removeGraphicalObjectListener(goListener);
        objects.remove(obj);
        if (obj.isSelected()) {
            selectedObjects.remove(obj);
        }
        notifyListeners();
    }


    /**
     * Read only list of currently stored objects.
     */
    public List<GraphicalObject> list() {
        return roObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        listeners.stream().forEach(DocumentModelListener::documentChanged);
    }

    /**
     * Read only list of currently selected objects.
     */
    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
    // Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)

    /**
     * Increase order when object is drawn for one (it will be painted later)
     */
    public void increaseZ(GraphicalObject go) {
        changeOrder(go, true);
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto ranije...

    /**
     * Decrease order when object is drawn for one (it will be painted sooner)
     */
    public void decreaseZ(GraphicalObject go) {
        changeOrder(go, false);
    }

    /**
     * Returns object which should be selected (enough close to <code>mousePoint</code>) or
     * null if there are no such object.
     */
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        Map<GraphicalObject, Double> objectDistance = new HashMap<>();
        double minDistance = Double.MAX_VALUE;
        for (GraphicalObject object : objects) {
            double distance = object.selectionDistance(mousePoint);
            if (Double.compare(distance, SELECTION_PROXIMITY) <= 0) {
                objectDistance.put(object, distance);
                if (Double.compare(distance, minDistance) < 0) {
                    minDistance = distance;
                }
            }
        }
        if (objectDistance.isEmpty()) {
            return null;
        }
        //find closest object(s)
        Map<GraphicalObject, Integer> objectOrder = new HashMap<>();
        double finalMinDistance = minDistance;
        objectDistance.entrySet()
                .stream()
                .filter(entry -> Double.compare(entry.getValue(), finalMinDistance) == 0)
                .forEach(entry -> {
                            objectOrder.put(entry.getKey(), objects.indexOf(entry.getKey()));
                        }
                );
        //find object with lowest order of painting
        Map<Integer, GraphicalObject> finalCandidates = new HashMap<>();
        int maxOrder = Integer.MIN_VALUE;
        for (Map.Entry<GraphicalObject, Integer> entry : objectOrder.entrySet()) {
            if (entry.getValue() > maxOrder) {
                maxOrder = entry.getValue();
                finalCandidates.put(maxOrder, entry.getKey());
            }
        }
        return finalCandidates.get(maxOrder);
    }

    /**
     * Return index of nearest hot point or -1 if distance to nearest point
     * is greater than <code>SELECTION_PROXIMITY</code>.
     */
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        Map<Double, Integer> distanceIndex = new HashMap<>();
        Double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
            double distance = object.getHotPointDistance(i, mousePoint);
            if (Double.compare(distance, minDistance) < 0) {
                minDistance = distance;
                distanceIndex.put(minDistance, i);
            }
        }
        if (Double.compare(minDistance, SELECTION_PROXIMITY) <= 0) {
            return distanceIndex.get(minDistance);
        } else {
            return -1;
        }
    }

    private void changeOrder(GraphicalObject go, boolean increase) {
        Objects.requireNonNull(go);
        int currentIndex = objects.indexOf(go);
        if (currentIndex == -1) {
            throw new IllegalArgumentException("Object is not in collection.");
        }
        //cannot change order of last elements
        if ((currentIndex == 0 && !increase) || currentIndex == objects.size() - 1 && increase) {
            return;
        }
        int nextIndex = increase ? currentIndex + 1 : currentIndex - 1;
        objects.set(currentIndex, objects.get(nextIndex));
        objects.set(nextIndex, go);
        notifyListeners();
    }
}
