package trasmapi.sumo;

import java.util.ArrayList;
import java.util.List;

public class SumoTrafficLightProgram {
    
    public class Phase {
        
        private int duration;
        private String state;
        
        public Phase(int duration, String state) {
            this.duration = duration;
            this.state = state;
        }
        
        public int getDuration() {
            return duration;
        }
        
        public String getState() {
            return state;
        }
    }

    private String id;
    private List<Phase> phases;
    
    public SumoTrafficLightProgram(String id) {
        this.id = id;
        this.phases = new ArrayList<Phase>();
    }
    
    public SumoTrafficLightProgram(ArrayList<Pair<Integer, Object>> items) {
        this((String) items.get(1).second());

        int numPhases = (int) items.get(5).second();
        for (int i = 0; i < numPhases; i++) {
            phases.add(new Phase((int) items.get(i * 4 + 6).second(),
                    (String) items.get(i * 4 + 9).second()));
        }
    }
    
    public String getId() {
        return id;
    }
    
    public List<Phase> getPhases() {
        return phases;
    }    
    
    public void addPhase(int duration, String state) {
        phases.add(new Phase(duration, state));
    }
    
    public int getDuration() {
        int duration = 0;
        for (Phase phase: phases) {
            duration += phase.getDuration();
        }
        return duration;
    }

}
