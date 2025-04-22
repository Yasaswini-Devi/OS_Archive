import java.util.*;

class Process {
    String pid;
    int arrivalTime;
    int burstTime;
    int turnAroundTime;
    int waitingTime;
    int completionTime;
    int remainingTime;
    int priority;
    
    Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = "P" + pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
    }
}

class ProcessScheduling {
    public static void printGantt(ArrayList<String> ganttChart, ArrayList<Integer> ganttTimes) {
        System.out.println("\nGantt Chart\n");
        for(String p: ganttChart) 
            System.out.printf("%5s", p);
        System.out.println();
        for(int t: ganttTimes)
            System.out.printf("%-5d", t);
    }
    
    public static void fcfs(int n, ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int prevCompletionTime = 0;
        
        ArrayList<String> ganttChart = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);
        
        for (Process p: processes) {
            if (prevCompletionTime < p.arrivalTime) {
                ganttChart.add("Idle");
                ganttTimes.add(p.arrivalTime);
                prevCompletionTime = p.arrivalTime;
            }
            p.completionTime = prevCompletionTime + p.burstTime;
            ganttChart.add(p.pid);
            ganttTimes.add(p.completionTime);
            p.turnAroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnAroundTime - p.burstTime;
            prevCompletionTime = p.completionTime;
        }
        
        System.out.println("\nCalculated");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s", "Process", "Arrival", "Burst", "Completion", "Turn Around", "Wait");
        for (Process p: processes) {
            System.out.printf("\n%-10s %-10s %-10s %-10s %-10s %-10s", p.pid, p.arrivalTime, p.burstTime, p.completionTime, p.turnAroundTime, p.waitingTime);
        }
        printGantt(ganttChart, ganttTimes);
    }
    
    public static void sjf(int n, ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> availableProcesses = new ArrayList<>();
        int available = 0, completed = 0;
        ArrayList<String> ganttChart = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);
        
        boolean idle = false;
        while (completed < n) {
            while(available < n && processes.get(available).arrivalTime <= currentTime) {
                availableProcesses.add(processes.get(available));
                available += 1;
            }
            
            if (availableProcesses.isEmpty()) {
                idle = true;
                currentTime += 1;
            }
            
            else {
                if (idle) {
                    ganttTimes.add(currentTime);
                    ganttChart.add("Idle");
                    idle = false;
                }
                
                Process selectedProcess = availableProcesses.stream().min(Comparator.comparingInt(p -> p.burstTime)).get();
                currentTime += selectedProcess.burstTime;
                selectedProcess.completionTime = currentTime;
                selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.burstTime;
                ganttTimes.add(currentTime);
                ganttChart.add(selectedProcess.pid);
                availableProcesses.remove(selectedProcess);
                completed += 1;
            }
        }
        System.out.println("\nCalculated");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s", "Process", "Arrival", "Burst", "Completion", "Turn Around", "Wait");
        for (Process p: processes) {
            System.out.printf("\n%-10s %-10s %-10s %-10s %-10s %-10s", p.pid, p.arrivalTime, p.burstTime, p.completionTime, p.turnAroundTime, p.waitingTime);
        }
        printGantt(ganttChart, ganttTimes);
    }
    
    public static void srtf(int n, ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> availableProcesses = new ArrayList<>();
        int available = 0, completed = 0;
        ArrayList<String> ganttChart = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);
        
        boolean idle = false;
        Process prevProcess = null;
        
        while (completed < n) {
            while(available < n && processes.get(available).arrivalTime <= currentTime) {
                availableProcesses.add(processes.get(available));
                available += 1;
            }
            
            if (availableProcesses.isEmpty()) {
                idle = true;
                currentTime += 1;
            }
            
            else {
                if (idle) {
                    ganttTimes.add(currentTime);
                    ganttChart.add("Idle");
                    idle = false;
                }
                
                Process selectedProcess = availableProcesses.stream().min(Comparator.comparingInt(p -> p.remainingTime)).get();
                currentTime += 1;
                selectedProcess.remainingTime -= 1;
                if (selectedProcess.remainingTime == 0) {                selectedProcess.completionTime = currentTime;
                    selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                    selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.burstTime;
                    availableProcesses.remove(selectedProcess);
                    completed += 1;
                }
                if (prevProcess == null)
                    prevProcess = selectedProcess;
                if (prevProcess != selectedProcess) {
                    ganttTimes.add(currentTime - 1);
                    ganttChart.add(prevProcess.pid);
                    prevProcess = selectedProcess;
                }
            }
        }
        ganttTimes.add(currentTime - 1);
        ganttChart.add(prevProcess.pid);
        
        System.out.println("\nCalculated");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s", "Process", "Arrival", "Burst", "Completion", "Turn Around", "Wait");
        for (Process p: processes) {
            System.out.printf("\n%-10s %-10s %-10s %-10s %-10s %-10s", p.pid, p.arrivalTime, p.burstTime, p.completionTime, p.turnAroundTime, p.waitingTime);
        }
        printGantt(ganttChart, ganttTimes);
    } 
    
    public static void priority(int n, ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int available = 0, completed = 0, currentTime = 0;
        boolean idle = false;
        
        ArrayList<Process> availableProcesses = new ArrayList<>();
        ArrayList<String> ganttChart = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);
        
        while (completed < n) {
            while (available < n && processes.get(available).arrivalTime <= currentTime) {
                availableProcesses.add(processes.get(available));
                available += 1;
            }
            
            if (availableProcesses.isEmpty()) {
                idle = true;
                currentTime += 1;
            }
            
            else {
                if (idle) {
                    ganttTimes.add(currentTime);
                    ganttChart.add("Idle");
                    idle = false;
                }
                Process selectedProcess = availableProcesses.stream().min(Comparator.comparingInt(p -> p.priority)).get();
                selectedProcess.completionTime = currentTime + selectedProcess.burstTime;
                completed += 1;
                currentTime += selectedProcess.burstTime;
                selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.arrivalTime;
                ganttTimes.add(currentTime);
                ganttChart.add(selectedProcess.pid);
                availableProcesses.remove(selectedProcess);
            }
        }
        printGantt(ganttChart, ganttTimes);
    }
    
    public static void pre_priority(int n, ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int available = 0, completed = 0, currentTime = 0;
        boolean idle = false;
        Process prevProcess = null;
        
        ArrayList<Process> availableProcesses = new ArrayList<>();
        ArrayList<String> ganttChart = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);
        
        while (completed < n) {
            while (available < n && processes.get(available).arrivalTime <= currentTime) {
                availableProcesses.add(processes.get(available));
                available += 1;
            }
            
            if (availableProcesses.isEmpty()) {
                idle = true;
                currentTime += 1;
            }
            
            else {
                if (idle) {
                    ganttTimes.add(currentTime);
                    ganttChart.add("Idle");
                    idle = false;
                }
                Process selectedProcess = availableProcesses.stream().min(Comparator.comparingInt(p -> p.priority)).get();
                currentTime += 1;
                selectedProcess.remainingTime -= 1;
                if (selectedProcess.remainingTime == 0) {                selectedProcess.completionTime = currentTime;
                    selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                    selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.burstTime;
                    availableProcesses.remove(selectedProcess);
                    completed += 1;
                }
                if (prevProcess == null)
                    prevProcess = selectedProcess;
                else if (prevProcess != selectedProcess) {
                    ganttTimes.add(currentTime - 1);
                    ganttChart.add(prevProcess.pid);
                    prevProcess = selectedProcess;
                }
            }
        }
        ganttTimes.add(currentTime);
        ganttChart.add(prevProcess.pid);
        
        printGantt(ganttChart, ganttTimes);
    }
    
    public static void roundRobin(int n, ArrayList<Process> processes, int quantum) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        Queue<Process> availableProcesses = new LinkedList<>();
        int available = 0, completed = 0, currentTime = 0;
        ArrayList<String> ganttChart = new ArrayList<>();
        ArrayList<Integer> ganttTimes = new ArrayList<>();
        ganttTimes.add(0);
        boolean idle = false;
        Process prevProcess = null;
        
        while (completed < n) {
            while (available < n && processes.get(available).arrivalTime <= currentTime) {
                availableProcesses.offer(processes.get(available));
                available += 1;
            }
            
            if (availableProcesses.isEmpty()) {
                if (prevProcess != null) {
                    ganttChart.add(prevProcess.pid);
                    ganttTimes.add(currentTime);
                    prevProcess = null;
                }
                idle = true;
                currentTime += 1;
            }
            else {
                if (idle) {
                    ganttChart.add("Idle");
                    ganttTimes.add(currentTime);
                    idle = false;
                }
                
                Process selectedProcess = availableProcesses.poll();
                int performed = Math.min(selectedProcess.remainingTime, quantum);
                selectedProcess.remainingTime -= performed;
                currentTime += performed;
                if (selectedProcess.remainingTime == 0) {
                    selectedProcess.completionTime = currentTime;
                    selectedProcess.turnAroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                    selectedProcess.waitingTime = selectedProcess.turnAroundTime - selectedProcess.burstTime;
                    completed += 1;
                }
                else {
                    availableProcesses.offer(selectedProcess);
                }
                
                if (prevProcess == null) {
                    prevProcess = selectedProcess;
                }
                else if (prevProcess != selectedProcess) {
                    ganttChart.add(prevProcess.pid);
                    ganttTimes.add(currentTime - performed);
                    prevProcess = selectedProcess;
                }
            }
        }
        ganttChart.add(prevProcess.pid);
        ganttTimes.add(currentTime);
        printGantt(ganttChart, ganttTimes);
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter no of processes: ");
        int n = sc.nextInt();
        
        ArrayList<Process> processes = new ArrayList<>();
        System.out.println("\nEnter arrival time and burst time for the following processes:");
        int arrivalTime, burstTime, priority;
        for(int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": ");
            arrivalTime = sc.nextInt();
            burstTime = sc.nextInt();
            priority = sc.nextInt();
            processes.add(new Process(i + 1, arrivalTime, burstTime, priority));
        }
        
        System.out.println("\nGiven processes");
        System.out.printf("%-10s %-10s %-10s", "Process", "Arrival", "Burst");
        for (Process p: processes) {
            System.out.printf("\n%-10s %-10d %-10d", p.pid, p.arrivalTime, p.burstTime);
        }
        fcfs(n, processes);
        sjf(n, processes);
        srtf(n, processes);
        priority(n, processes);
        pre_priority(n, processes);
        roundRobin(n, processes, 5);
    }
}
