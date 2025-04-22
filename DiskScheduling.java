import java.util.*;
import java.awt.*;
import javax.swing.*;

class DiskScheduling extends JPanel {
    ArrayList<Integer> headMovements = new ArrayList<>();
    DiskScheduling(ArrayList<Integer> headMovements) {
        this.headMovements = headMovements;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int graphHeight = height - 2 * padding;
        int graphWidth = width - 2 * padding;

        int maxCylinder = Collections.max(headMovements);
        int minCylinder = Collections.min(headMovements);

        g.drawLine(padding, padding, padding, height - padding);
        g.drawLine(padding, height - padding, width - padding, height - padding);
        g.setColor(Color.GREEN);

        int totalMovements = 0;

        for(int i = 0; i < headMovements.size() - 1; i++) {
            int x1 = padding + (i * graphWidth / (headMovements.size() - 1));
            int y1 = height - padding - (headMovements.get(i) - minCylinder) * graphHeight / (maxCylinder - minCylinder);
            int x2 = padding + ((i + 1) * graphWidth / (headMovements.size() - 1));
            int y2 = height - padding - (headMovements.get(i + 1) - minCylinder) * graphHeight / (maxCylinder - minCylinder);

            g.drawLine(x1, y1, x2, y2);
            g.fillOval(x1 - 3, y1 - 3, 6, 6);

            g.setColor(Color.BLUE);
            g.drawString(String.valueOf(headMovements.get(i)), x1 - 10, y1 - 10);
            g.setColor(Color.GREEN);
            totalMovements += Math.abs(headMovements.get(i + 1) - headMovements.get(i));
        }

        int lastX = padding + ((headMovements.size() - 1) * graphWidth / (headMovements.size() - 1));
        int lastY = height - padding - (headMovements.get(headMovements.size() - 1) - minCylinder) * graphHeight / (maxCylinder - minCylinder);
        g.fillOval(lastX - 3, lastY - 3, 6, 6);
        g.setColor(Color.BLUE);
        g.drawString(String.valueOf(headMovements.get(headMovements.size() - 1)), lastX - 10, lastY - 10);
        g.setColor(Color.RED);
        g.drawString("Total Head Movements: " + totalMovements, padding, height - padding + 30);
    }

    public static void fcfs(int []requests, int head, ArrayList<Integer> headMovements) {
        for (int r: requests)
            headMovements.add(r);
    }

    public static void sstf(int []requests, int head, ArrayList<Integer> headMovements) {
        int n = requests.length;
        boolean[] visited = new boolean[n];
        for(int i = 0; i < n; i ++) {
            int min_dist = Integer.MAX_VALUE, min_idx = -1;
            for(int j = 0; j < n; j++) {
                if (visited[j])
                    continue;
                int dist = Math.abs(requests[j] - head);
                if (dist < min_dist) {
                    min_dist = dist;
                    min_idx = j;
                }
            }
            headMovements.add(requests[min_idx]);
            visited[min_idx] = true;
            head = requests[min_idx];
        }
    }

    public static void scan(int[] requests, int head, ArrayList<Integer> headMovements) {
        Arrays.sort(requests);
        for (int i = requests.length - 1; i >= 0; i--)
            if (requests[i] <= head)
                headMovements.add(requests[i]);
        headMovements.add(0);
        for(int i = 0; i < requests.length; i++)
            if (requests[i] > head)
                headMovements.add(requests[i]);
    }

    public static void cscan(int[] requests, int head, ArrayList<Integer> headMovements) {
        Arrays.sort(requests);
        for (int i = requests.length - 1; i >= 0; i--)
            if (requests[i] <= head)
                headMovements.add(requests[i]);
        headMovements.add(0);
        headMovements.add(199);
        for(int i = requests.length - 1; i >= 0; i--)
            if (requests[i] > head)
                headMovements.add(requests[i]);
    }

    public static void look(int[] requests, int head, ArrayList<Integer> headMovements) {
        Arrays.sort(requests);
        for (int i = requests.length - 1; i >= 0; i--)
            if (requests[i] <= head)
                headMovements.add(requests[i]);
        for(int i = 0; i < requests.length; i++)
            if (requests[i] > head)
                headMovements.add(requests[i]);
    }

    public static void clook(int[] requests, int head, ArrayList<Integer> headMovements) {
        Arrays.sort(requests);
        for (int i = requests.length - 1; i >= 0; i--)
            if (requests[i] <= head)
                headMovements.add(requests[i]);
        for(int i = requests.length - 1; i >= 0; i--)
            if (requests[i] > head)
                headMovements.add(requests[i]);
    }

    public static void main(String []args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of disk requests: ");
        int n = sc.nextInt();

        int []requests = new int[n];
        Random rand = new Random();

        System.out.println("Generated Disk Requests: ");
        for(int i = 0; i < n; i++) {
            requests[i] = rand.nextInt(200);
            System.out.print(requests[i] + " ");
        }
        System.out.println();

        System.out.print("Enter initial head position: ");
        int head = sc.nextInt();
        ArrayList<Integer> headMovements = new ArrayList<>();
        headMovements.add(head);

        fcfs(requests, head, headMovements);
        sstf(requests, head, headMovements);
        scan(requests, head, headMovements);
        cscan(requests, head, headMovements);
        look(requests, head, headMovements);
        clook(requests, head, headMovements);

        JFrame frame = new JFrame("Disk Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new DiskScheduling(headMovements));
        frame.setVisible(true);
        sc.close();
    }
}
