import java.util.*;

class PageReplacement {
    public static void fifo(ArrayList<Integer> pages, int capacity) {
        ArrayList<Integer> frame = new ArrayList<>();
        int i = 0;
        for(int page: pages) {
            if (!frame.contains(page)) {
                if (frame.size() < capacity)
                    frame.add(page);
                else
                    frame.set(i, page);
                i = (i + 1) % capacity;
            }
            System.out.println("Page " + page + " " + frame);
        }
    }
    
    public static void lru(ArrayList<Integer> pages, int capacity) {
        ArrayList<Integer> frame = new ArrayList<>();
        HashMap<Integer, Integer> lastUsed = new HashMap<>();
        int i = 0;
        for(int page: pages) {
            if (!frame.contains(page)) {
                if (frame.size() < capacity)
                    frame.add(page);
                else{
                    int leastRecent = i;
                    int replacePage = -1;
                    for (int f: frame) {
                        if (lastUsed.get(f) < leastRecent) {
                            leastRecent = lastUsed.get(f);
                            replacePage = f;
                        }
                    }
                    frame.set(frame.indexOf(replacePage), page);
                }
            }
            lastUsed.put(page, i);
            i += 1;
            System.out.println("Page " + page + " " + frame);
        }
    }
    
    public static void optimal(ArrayList<Integer> pages, int capacity) {
        ArrayList<Integer> frame = new ArrayList<>();
        for(int i = 0; i < pages.size(); i++) {
            int page = pages.get(i);
            if (!frame.contains(page)) {
                if (frame.size() < capacity)
                    frame.add(page);
                else{
                    int next = -1, farthest = -1, p = -1;
                    for (int f: frame) {
                        for(int j = i + 1; j < pages.size(); j++) {
                            if (pages.get(j) == f) {
                                next = j;
                                break;
                            }
                        }
                        if (next == -1) {
                            next = pages.size();
                        }
                        if (next > farthest) {
                            farthest = next;
                            p = f;
                        }
                    }
                    frame.set(frame.indexOf(p), page);
                }
            }
            System.out.println("Page " + page + " " + frame);
        }
    }
    
    public static void main(String []args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of pages");
        int pageCount = sc.nextInt();
        Random rand = new Random();
        ArrayList<Integer> pages = new ArrayList<>();
        for(int i = 0; i < pageCount; i++)
            pages.add(rand.nextInt(10));
        System.out.print(pages);
        
        System.out.println();
        
        System.out.print("Enter frame count: ");
        int frameCount = sc.nextInt();
        System.out.println();
        fifo(pages, frameCount);
        System.out.println();
        lru(pages, frameCount);
        System.out.println();
        optimal(pages, frameCount);
    }
}
