import java.util.*;

class Block {
    int id, size, remainingSize;
    String allocated;
    
    Block(int id, int size, String allocated) {
        this.id = id;
        this.size = size;
        this.allocated = allocated;
    }
    
    public Block splitBlock(int size) {
        int remainingSize = this.size - size;
        this.size = size;
        return new Block(++Main.blockCount, remainingSize, "");
    }
}

class Process {
    String id;
    int size, allocated = -1;
    Process (int id, int size) {
        this.id = "P" + id;
        this.size = size;
    }
}

class MemoryAllocation {
    static ArrayList<Block> blocks = new ArrayList<>();
    static ArrayList<Process> processes = new ArrayList<>();
    
    static Random random = new Random();
    static int blockCount = 0;
    
    public static void printBlocks() {
        for (Block b: blocks) {
            if (b.allocated == "Filled")
                System.out.print("Filled");
            else if (b.allocated == "")
                System.out.print(b.size);
            else
                System.out.print(b.allocated);
            System.out.print(" | ");
        }
    }
    
    public static void printRemProcesses() {
        System.out.println("Rem Processes:");
        for (Process p: processes)
            if (p.allocated == -1)
                System.out.print(p.id);
        System.out.println();
    }
    
    public static void worstFit() {
        for (Process p: processes) {
            Block max_block = null;
            int idx = -1;
            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b.allocated != "")
                    continue;
                if (b.size >= p.size) {
                    if (max_block == null) {
                        max_block = b;
                        idx = i + 1;
                    } else {
                        if (max_block.size < b.size) {
                            max_block = b;
                            idx = i + 1;
                        }
                    }
                }
            }
            if (max_block == null)
                break;
            max_block.allocated = p.id;
            blocks.add(idx, max_block.splitBlock(p.size));
            p.allocated = max_block.id;
        }
    }
    
    public static void bestFit() {
        for (Process p: processes) {
            Block min_block = null;
            int idx = -1;
            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b.allocated != "")
                    continue;
                if (b.size >= p.size) {
                    if (min_block == null) {
                        min_block = b;
                        idx = i + 1;
                    } else {
                        if (min_block.size > b.size) {
                            min_block = b;
                            idx = i + 1;
                        }
                    }
                }
            }
            if (min_block == null)
                break;
            min_block.allocated = p.id;
            blocks.add(idx, min_block.splitBlock(p.size));
            p.allocated = min_block.id;
        }
    }
    
    public static void firstFit() {
        for (Process p: processes) {
            for (int i = 0; i < blocks.size(); i++) {
                Block b = blocks.get(i);
                if (b.allocated != "")
                    continue;
                if (b.size >= p.size) {
                    b.allocated = p.id;
                    blocks.add(i + 1, b.splitBlock(p.size));
                    p.allocated = b.id;
                    break;
                }
            }
        }
        printRemProcesses();
    }
    
    public static void main(String []args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter block count: ");
        blockCount = sc.nextInt();
        System.out.print("\nEnter process count: ");
        int processCount = sc.nextInt();
        for(int i = 0; i < blockCount; i++) {
            int filled = random.nextInt(2);
            int size = random.nextInt(150) + 1;
            if (filled == 1) 
                blocks.add(new Block(i + 1, size, "Filled"));
            else
                blocks.add(new Block(i + 1, size, ""));
        }
        System.out.println("\nGenerated Blocks:");
        printBlocks();
        System.out.println();
        
        for(int i = 0; i < processCount; i++) {
            int size = random.nextInt(100) + 5;
            processes.add(new Process(i + 1, size));
        }
        
        System.out.println("Generated Processes:");
        for (Process p: processes) {
            System.out.println(p.id + " " + p.size);
        }
        
        firstFit();
        bestFit();
        worstFit();
        printBlocks();
        printRemProcesses();
    }
}
