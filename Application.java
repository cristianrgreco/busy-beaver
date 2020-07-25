import java.util.*;

class Application {
    public static void main(String[] args) {
        int states = Integer.parseInt(args[0]);

        var busyBeaver = BusyBeaver.fromStates(states);
        var result = busyBeaver.run(new Tape());
        System.out.println(result);
    }

    static class BusyBeaver {
        private static Map<Integer, BusyBeaver> BUSY_BEAVERS = Map.of(
                1, new BusyBeaver(Map.of(
                        "A0", new TuringMachine("1RH")
                )),
                2, new BusyBeaver(Map.of(
                        "A0", new TuringMachine("1RB"),
                        "A1", new TuringMachine("1LB"),
                        "B0", new TuringMachine("1LA"),
                        "B1", new TuringMachine("1RH")
                )),
                3, new BusyBeaver(Map.of(
                        "A0", new TuringMachine("1RB"),
                        "A1", new TuringMachine("1RH"),
                        "B0", new TuringMachine("0RC"),
                        "B1", new TuringMachine("1RB"),
                        "C0", new TuringMachine("1LC"),
                        "C1", new TuringMachine("1LA")
                )),
                4, new BusyBeaver(Map.of(
                        "A0", new TuringMachine("1RB"),
                        "A1", new TuringMachine("1LB"),
                        "B0", new TuringMachine("1LA"),
                        "B1", new TuringMachine("0LC"),
                        "C0", new TuringMachine("1RH"),
                        "C1", new TuringMachine("1LD"),
                        "D0", new TuringMachine("1RD"),
                        "D1", new TuringMachine("0RA")
                )),
                5, new BusyBeaver(Map.of(
                        "A0", new TuringMachine("1RB"),
                        "A1", new TuringMachine("1LC"),
                        "B0", new TuringMachine("1RC"),
                        "B1", new TuringMachine("1RB"),
                        "C0", new TuringMachine("1RD"),
                        "C1", new TuringMachine("0LE"),
                        "D0", new TuringMachine("1LA"),
                        "D1", new TuringMachine("1LD"),
                        "E0", new TuringMachine("1RH"),
                        "E1", new TuringMachine("0LA")
                ))

        );

        static BusyBeaver fromStates(int states) {
            return BUSY_BEAVERS.get(states);
        }

        private final Map<String, TuringMachine> turingMachines;

        private char state;
        private int steps;

        BusyBeaver(Map<String, TuringMachine> turingMachines) {
            this.turingMachines = turingMachines;
            this.state = 'A';
            this.steps = 0;
        }

        String run(Tape tape) {
            while (this.state != 'H') {
                var symbol = tape.getValue();
                var key = "" + this.state + symbol;
                var turingMachine = this.turingMachines.get(key);

                var nextSymbol = turingMachine.getSymbol();
                var nextDirection = turingMachine.getDirection();
                var nextState = turingMachine.getState();

                tape.setValue(nextSymbol);
                if (nextDirection == 'L') {
                    tape.moveLeft();
                } else {
                    tape.moveRight();
                }
                this.steps++;
                this.state = nextState;
            }
            
            return String.format("Complete in %d steps, %s ones written to tape:\n%s", this.steps, tape.getOnes(), tape);
        }
    }

    static class TuringMachine {
        private String machine;

        TuringMachine(String machine) {
            this.machine = machine;
        }

        int getSymbol() {
            return Character.getNumericValue(this.machine.charAt(0));
        }

        char getDirection() {
            return this.machine.charAt(1);
        }

        char getState() {
            return this.machine.charAt(2);
        }

        @Override
        public String toString() {
            return this.machine;
        }
    }

    static class Tape {
        private final List<Integer> tape;

        private int pos;
        private int size;

        Tape() {
            this.pos = 0;
            this.tape = new ArrayList<>();
            this.tape.add(0);
        }

        int getValue() {
            return this.tape.get(this.pos);
        }

        void setValue(int value) {
            this.tape.set(this.pos, value);
        }

        void moveLeft() {
            if (this.pos > 0) {
                this.pos--;
            } else {
                this.tape.add(this.pos, 0);
            }
        }

        void moveRight() {
            this.pos++;
            if (this.pos == this.tape.size()) {
                this.tape.add(0);
            }
        }

        int getOnes() {
            return (int) this.tape.stream().filter(x -> x == 1).count();
        }

        @Override
        public String toString() {
            return this.tape.toString();
        }
    }
}
