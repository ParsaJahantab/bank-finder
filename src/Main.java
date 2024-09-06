import java.util.Scanner;

class Stack_Node{
    int []coordinates;
    String cm;
    Bank_Main bank_main;
    Bank_Branch bank_branch;
    Neighborhood neighborhood;
    Stack_Node(String cm,Bank_Main bank_main,Bank_Branch bank_branch,int[] coordinates1,Neighborhood neighborhood){
        this.cm=cm;
        this.bank_main=bank_main;
        this.bank_branch=bank_branch;
        this.neighborhood=neighborhood;
        coordinates=new int[2];
        if (coordinates1!=null)
        System.arraycopy(coordinates1, 0, coordinates, 0, 2);
    }
}
class Stack {

    int Stack_cap;
    Stack_Node[] Stack;
    int Stack_top;
    Stack() {
        Stack_cap = 100;
        Stack = new Stack_Node[Stack_cap];
        Stack_top = 0;
    }

    public void push(Stack_Node data) {
        if (Stack_top == Stack_cap) {
            Stack_Node[] temp_stack = new Stack_Node[Stack_cap * 2];
            if (Stack_cap >= 0) System.arraycopy(Stack, 0, temp_stack, 0, Stack_cap);
            Stack = temp_stack;
            temp_stack = null;
            Stack_cap = Stack_cap * 2;
            Stack[Stack_top] = data;
            Stack_top = Stack_top + 1;
        } else {
            Stack[Stack_top] = data;
            Stack_top = Stack_top + 1;
        }
    }

    public Stack_Node pop() {
        if (Stack_top != 0) {
            Stack_Node stack_node= Stack[Stack_top - 1];
            Stack[Stack_top - 1] = null;
            Stack_top = Stack_top - 1;
            return stack_node;
        }
        return null;
    }

    public Stack_Node peek() {
        return (Stack[Stack_top - 1]);
    }

    public boolean empty() {
        return Stack_top == 0;
    }
}
class Neighborhood {
    String name;
    int [] point1;
    int [] point2;
    Neighborhood(int [] point1,int [] point2,String name)
    {
        this.name=name;
        this.point1=point1;
        this.point2=point2;
    }
}

class Bank_Main {
    int []coordinates;
    String Name;
    KdTree branches;
    Bank_Main(String Name,int[] coordinates)
    {
        this.Name=Name;
        this.coordinates=coordinates;
        branches=new KdTree();
    }
}
class Bank_Branch{
    String Bank_Name;
    String Branch_Name;
    int []coordinates;
    Bank_Branch(String Bank_Name,String Branch_Name,int [] coordinates)
    {
        this.Bank_Name=Bank_Name;
        this.Branch_Name=Branch_Name;
        this.coordinates=coordinates;
    }
}
class Node{
    Bank_Main bank_main;
    Bank_Branch bank_branch;
    int []coordinates;
    Node left;
    Node right;
    Node(int [] coordinates)
    {
        this.bank_main=null;
        this.bank_branch=null;
        this.left=null;
        this.right=null;
        this.coordinates=coordinates;
    }
    Node(Bank_Main bank_main)
    {
        this.bank_main=bank_main;
        bank_branch=null;
        this.coordinates= new int[2];
        System.arraycopy(bank_main.coordinates, 0, coordinates, 0, 2);
        this.left=null;
        this.right=null;
    }
    Node(Bank_Main bank_main,Bank_Branch bank_branch)
    {
        this.bank_main=bank_main;
        this.bank_branch=bank_branch;
        this.coordinates=new int[2];
        System.arraycopy(bank_branch.coordinates, 0, coordinates, 0, 2);
        this.left=null;
        this.right=null;
    }
}
class KdTree{
    Boolean A=true;
    Node root;
    int size;
    Node delete_find;
    Boolean exists=false;
    Boolean deletion=true;
    Boolean undo=false;
    public void insert(Node point)
    {
        root=insert(root,point,0);
        size=size+1;
    }
    private Node insert(Node current,Node add_node,int depth)
    {
        if (current==null)
        {
            if (add_node.bank_branch==null)
            {
                current=new Node(add_node.bank_main);
            }
            else
            {
                current=new Node(add_node.bank_main, add_node.bank_branch);
                if (!undo)
                    add_node.bank_main.branches.insert_branch(add_node);
            }

            return current;
        }

        int dimension=depth%2;
        if (same(current,add_node))
        {
            System.out.println("the bank already exists");
            exists=true;
            return current;
        }
        else if (add_node.coordinates[dimension] < current.coordinates[dimension])
        {
            current.left=insert(current.left,add_node,depth+1);
        }
        else
        {
            current.right=insert(current.right,add_node,depth+1);
        }
        return current;
    }
    private boolean same(Node point1,Node point2)
    {
        for (int i = 0; i < 2; i++) {
            if (point1.coordinates[i]!=point2.coordinates[i])
            {
                return false;
            }
        }
        return true;
    }
    public void insert_branch(Node point)
    {
        root=insert_branch(root,point,0);
        size=size+1;
    }
    private Node insert_branch(Node current,Node add_node,int depth)
    {
        if (current==null)
        {
            current=new Node(add_node.bank_main, add_node.bank_branch);
            return current;
        }
        int dimension=depth%2;
        if (add_node.coordinates[dimension] < current.coordinates[dimension])
        {
            current.left=insert_branch(current.left,add_node,depth+1);
        }
        else
        {
            current.right=insert_branch(current.right,add_node,depth+1);
        }
        return current;
    }
    public void print_all()
    {
        print_all(root);
    }
    private void print_all(Node current)
    {
        if (current==null)
        {
            return;
        }
        print_all(current.left);
        if (current.bank_branch==null)
            System.out.println("main bank name: "+current.bank_main.Name+" coordinates:  x:"+current.coordinates[0]+" y:"+current.coordinates[1]);
        else
            System.out.println("main bank name: "+current.bank_main.Name+" branch name:"+current.bank_branch.Branch_Name+" coordinates:  x:"+current.coordinates[0]+" y:"+current.coordinates[1]);
        print_all(current.right);
    }
    public Node nearest(int [] target_cor)
    {
        Node target=new Node(target_cor);
        return nearest(root,target,0);
    }
    private Node nearest(Node current,Node target,int depth)
    {
        if (current==null)
        {
            return null;
        }
        Node next_branch;
        Node other_branch;
        int dimension=depth%2;
        if (target.coordinates[dimension]<current.coordinates[dimension])
        {
            next_branch = current.left;
            other_branch = current.right;
        } else {
            next_branch = current.right;
            other_branch = current.left;
        }
        Node temp_node=nearest(next_branch,target,depth+1);
        Node nearest_node= nearest_2nodes(temp_node,current,target);
        int radius = (int) distance(target, nearest_node);
        int dist = target.coordinates[depth%2] - current.coordinates[depth%2];

        if (radius >= dist) {
            temp_node = nearest(other_branch, target, depth + 1);
            nearest_node = nearest_2nodes(temp_node, nearest_node, target);
        }
        return nearest_node;
    }
    private Node nearest_2nodes(Node n0, Node n1, Node target) {
        if (n0 == null) {
            return n1;
        }
        else if (n1 == null) {
            return n0;
        }
        int distance1 = (int) distance(n0, target);
        int distance2 = (int) distance(n1, target);
        return distance1<distance2 ? n0 :n1;
    }
    private static double distance(Node point1, Node point2) {
        int distance = 0;

        for (int i = 0; i < 2; i++) {
            int dis = Math.abs(point1.coordinates[i] - point2.coordinates[i]);
            distance= (int) (distance+Math.pow(dis, 2));
        }
        return  Math.sqrt(distance);
    }
    private Node Min_in_3(Node node1, Node node2, Node node3, int d)
    {
        Node result=node1;
        if (node2!=null && node2.coordinates[d] < result.coordinates[d])
        {
            result=node2;
        }
        if (node3!=null && node3.coordinates[d]<result.coordinates[d])
        {
            result=node3;
        }
        return result;
    }
    private Node MinNode(Node root, int d)
    {
        return MinNode(root,d,0);
    }
    private Node MinNode(Node current, int d, int depth)
    {
        if (current==null)
        {
            return null;
        }
        int dimension=depth%2;
        if (dimension==d)
        {
            if (current.left==null)
            {
                return current;
            }
            return MinNode(current.left,d,depth+1);
        }
        return Min_in_3(current, MinNode(current.left,d,depth+1), MinNode(current.right,d,depth+1),d);
    }
    private void ReplaceNode(Node point1, Node point2)
    {
        point1.bank_main= point2.bank_main;
        point1.bank_branch= point2.bank_branch;
        System.arraycopy(point2.coordinates, 0, point1.coordinates, 0, 2);
    }
    private Node deletebr(Node current,Node target,int depth)
    {
        if (current==null)
            return null;
        int dimension=depth%2;
        if (same(current,target)) {
            if (current.right != null) {
                Node min = MinNode(current.right, dimension);
                ReplaceNode(current, min);
                current.right = deletebr(current.right, min, depth + 1);
            } else if (current.left != null) {
                Node min = MinNode(current.left, dimension);
                ReplaceNode(current, min);
                current.right = deletebr(current.left, min, depth + 1);
                current.left=null;
            } else {
                return null;
            }
        }
        if (target.coordinates[dimension]<current.coordinates[dimension])
        {
            current.left=deletebr(current.left,target,depth+1);
        }
        else
        {
            current.right=deletebr(current.right,target,depth+1);
        }
        return current;
    }
    private Bank_Main find(Node root,Node point,int depth)
    {
        while (!same(root,point))
        {
            int cd=depth%2;
            if (point.coordinates[cd]<root.coordinates[cd])
            {
                root=root.left;
            }
            else
            {
                root=root.right;
            }
            depth=depth+1;
        }
        return root.bank_main;
    }
    private boolean delete_check(Node root,Node point,int depth)
    {
        if (root==null)
        {
            deletion=false;
            return false;
        }
        if (same(root,point))
        {
            if (root.bank_branch==null)
            {
                System.out.println("this is a main bank");
                A=false;
                deletion=false;
                return false;
            }
            delete_find=root;
            return true;
        }
        int cd=depth%2;
        if (point.coordinates[cd] < root.coordinates[cd])
        {
            return delete_check(root.left,point,depth+1);
        }
            return delete_check(root.right,point,depth+1);
    }
    private boolean delete_check(Node point)
    {
        return delete_check(root,point,0);
    }
    public void delete(int[] point1)
    {
        Node point=new Node(point1);
        if (delete_check(point)) {
            size=size-1;
            Bank_Main bank_main=find(root,point,0);
            root=deletebr(root, point, 0);
            bank_main.branches.root=deletebr(bank_main.branches.root,point,0);
            bank_main.branches.size= bank_main.branches.size-1;
        }
        else
        {
            if (A)
            {
                System.out.println("no bank exists at this point");
            }
        }
        A=true;
    }
    private void circle(Node current,Node point,int radius,int depth)
    {
        if (current==null)
        {
            return;
        }
        int cd=depth%2;
        if (distance(current,point)<=radius)
        {
            System.out.println("bank: "+current.bank_main.Name);
            if (current.bank_branch==null)
                System.out.println("main bank name: "+current.bank_main.Name+" coordinates:  x:"+current.coordinates[0]+" y:"+current.coordinates[1]);
            else
                System.out.println("main bank name: "+current.bank_main.Name+" branch name:"+current.bank_branch.Branch_Name+" coordinates:  x:"+current.coordinates[0]+" y:"+current.coordinates[1]);
        }
        if (current.coordinates[cd]>(point.coordinates[cd]-radius))
        {
            circle(current.left, point, radius, depth+1);
        }
        if (current.coordinates[cd]<(point.coordinates[cd]+radius))
        {
            circle(current.right, point, radius, depth+1);
        }
    }
    public void circle(int [] point1,int radius)
    {
        Node point=new Node(point1);
        circle(root,point,radius,0);
    }
    private void InNeighborhood(Neighborhood neighborhood,Node current,int depth)
    {
        if (current==null)
        {
            return;
        }
        int dimension=depth%2;
        if (CheckInNeighborhood(neighborhood,current))
        {
            if (current.bank_branch==null)
                System.out.println("main bank name: "+current.bank_main.Name+" coordinates:  x:"+current.coordinates[0]+" y:"+current.coordinates[1]);
            else
                System.out.println("main bank name: "+current.bank_main.Name+" branch name:"+current.bank_branch.Branch_Name+" coordinates:  x:"+current.coordinates[0]+" y:"+current.coordinates[1]);

        }
        if ((current.coordinates[dimension]<=(neighborhood.point2[dimension]))&& current.coordinates[dimension]>=(neighborhood.point1[dimension]))
        {
            InNeighborhood(neighborhood,current.left,depth+1);
            InNeighborhood(neighborhood,current.right,depth+1);
        }
        else if (current.coordinates[dimension]>(neighborhood.point2[dimension]))
        {
            InNeighborhood(neighborhood,current.left,depth+1);
        }
        else if (current.coordinates[dimension]<(neighborhood.point1[dimension]))
        {
            InNeighborhood(neighborhood,current.right,depth+1);
        }

    }
    private boolean CheckInNeighborhood(Neighborhood neighborhood,Node current)
    {
        for (int i = 0; i < 2; i++) {
            if((current.coordinates[i]<neighborhood.point1[i]) || (current.coordinates[i]>neighborhood.point2[i]))
            {
                return false;
            }
        }
        return true;
    }
    public void InNeighborhood(Neighborhood neighborhood)
    {
        InNeighborhood(neighborhood,root,0);
    }
}
class TrieNode {
    TrieNode[] children = new TrieNode[63];
    boolean isEndOfWord;
    Bank_Main bank_main;
    Neighborhood neighborhood;
    TrieNode()
    {
        bank_main=null;
        isEndOfWord = false;
        for (int i = 0; i < 63; i++)
            children[i] = null;
    }

    public void setBank_main(Bank_Main bank_main) {
        this.bank_main = bank_main;
    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }
}
class Trie_Tree {
    TrieNode root;
    Trie_Tree(){
        root=new TrieNode();
    }
    public void insert(Bank_Main bank_main,Neighborhood neighborhood) {
        String name;
        if (neighborhood==null) {
            name = bank_main.Name;
        }
        else
        {
            name=neighborhood.name;
        }
        int level;
        int length = name.length();
        int index = 0;
        TrieNode current = root;
        for (level = 0; level < length; level++) {
            if (name.charAt(level) >= 48 && name.charAt(level) <= 57) {
                index = name.charAt(level) - '0';
            } else if (name.charAt(level) >= 65 && name.charAt(level) <= 90) {
                index = name.charAt(level) - 'A' + 10;
            } else if (name.charAt(level) >= 97 && name.charAt(level) <= 122) {
                index = name.charAt(level) - 'a' + 36;
            } else if (name.charAt(level) == ' ') {
                index = 63;
            }
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
        current.setBank_main(bank_main);
        current.setNeighborhood(neighborhood);
    }
    public boolean check(Bank_Main bank_main,Neighborhood neighborhood) {
        String name;
        if (neighborhood==null) {
            name = bank_main.Name;
        }
        else
        {
            name=neighborhood.name;
        }
        int level;
        int length = name.length();
        int index = 0;
        TrieNode current = root;
        for (level = 0; level < length; level++) {
            if (name.charAt(level) >= 48 && name.charAt(level) <= 57) {
                index = name.charAt(level) - '0';
            } else if (name.charAt(level) >= 65 && name.charAt(level) <= 90) {
                index = name.charAt(level) - 'A' + 10;
            } else if (name.charAt(level) >= 97 && name.charAt(level) <= 122) {
                index = name.charAt(level) - 'a' + 36;
            } else if (name.charAt(level) == ' ') {
                index = 63;
            }
            if (current!=null)
                current = current.children[index];
            if (current==null)
                return true;
        }
        if (current.isEndOfWord)
        {
            if (neighborhood==null)
                System.out.println("a bank with this name already exists");
            else
                System.out.println("a neighborhood with this name already exists");
            return false;
        }
        else {
            return true;
        }
    }
    public Bank_Main getBank(String bank_name) {
        int level;
        int length = bank_name.length();
        int index = 0;
        TrieNode current = root;
        for (level = 0; level < length; level++) {
            if (bank_name.charAt(level) >= 48 && bank_name.charAt(level) <= 57) {
                index = bank_name.charAt(level) - '0';
            } else if (bank_name.charAt(level) >= 65 && bank_name.charAt(level) <= 90) {
                index = bank_name.charAt(level) - 'A' + 10;
            } else if (bank_name.charAt(level) >= 97 && bank_name.charAt(level) <= 122) {
                index = bank_name.charAt(level) - 'a' + 36;
            } else if (bank_name.charAt(level) == ' ') {
                index = 63;
            }
            if (current.children[index]!=null)
                current = current.children[index];
            else
            {
                System.out.println("this bank doesn't exist");
                return null;
            }
        }
        return current.bank_main;
    }
    public Neighborhood getNeighborhood(String neighborhood_name) {
        int level;
        int length = neighborhood_name.length();
        int index = 0;
        TrieNode current = root;
        for (level = 0; level < length; level++) {
            if (neighborhood_name.charAt(level) >= 48 && neighborhood_name.charAt(level) <= 57) {
                index = neighborhood_name.charAt(level) - '0';
            } else if (neighborhood_name.charAt(level) >= 65 && neighborhood_name.charAt(level) <= 90) {
                index = neighborhood_name.charAt(level) - 'A' + 10;
            } else if (neighborhood_name.charAt(level) >= 97 && neighborhood_name.charAt(level) <= 122) {
                index = neighborhood_name.charAt(level) - 'a' + 36;
            } else if (neighborhood_name.charAt(level) == ' ') {
                index = 63;
            }
            if (current.children[index]!=null)
                current = current.children[index];
            else
            {
                System.out.println("this neighborhood doesn't exist");
                return null;
            }
        }
        return current.neighborhood;
    }
}
public class Main {
    public static void addN(Stack stack,Trie_Tree neighborhoods,String name,int [] point1,int [] point2)
    {
        Neighborhood neighborhood=new Neighborhood(point1,point2,name);
        if (neighborhoods.check(null,neighborhood)) {
            neighborhoods.insert(null, neighborhood);
            Stack_Node stack_node=new Stack_Node("addN",null,null,null,neighborhood);
            stack.push(stack_node);
        }
    }
    public static void addB(Stack stack,KdTree kdTree,Trie_Tree trie_tree,Bank_Main bank_main)
    {
        Node node=new Node(bank_main);
        if (!kdTree.exists && trie_tree.check(bank_main,null)) {
            kdTree.insert(node);
            if (!kdTree.exists) {
                trie_tree.insert(bank_main, null);
                Stack_Node stack_node=new Stack_Node("addB",bank_main,null,null,null);
                stack.push(stack_node);
            }
            else
                kdTree.exists=false;
        }
    }
    public static void addBr(Stack stack,KdTree kdTree,Trie_Tree trie_tree,String Bank_name,Bank_Branch bank_branch,Boolean undo)
    {
        Bank_Main bank_main=trie_tree.getBank(Bank_name);
        if (undo)
        {
            kdTree.undo=true;
        }
        if (bank_main!=null)
        {
            Node node = new Node(bank_main, bank_branch);
            kdTree.insert(node);
            Stack_Node stack_node=new Stack_Node("addBr",bank_main,bank_branch,null,null);
            stack.push(stack_node);
        }
        kdTree.undo=false;
    }
    public static void delBr(Stack stack,KdTree kdTree,int []point)
    {
        if (kdTree.deletion) {
            kdTree.delete(point);
            Stack_Node stack_node = new Stack_Node("delBr", null, null, point, null);
            stack.push(stack_node);

        }
        else
        {
            kdTree.deletion=true;
        }
    }
    public static void ListB(Stack stack,KdTree kdTree,Trie_Tree trie_tree,String name)
    {
        Neighborhood neighborhood=trie_tree.getNeighborhood(name);
        if (neighborhood!=null) {
            kdTree.InNeighborhood(neighborhood);
            Stack_Node stack_node = new Stack_Node("listB", null, null, null, null);
            stack.push(stack_node);
        }
    }
    public static void ListBrs(Stack stack,Trie_Tree trie_tree,String name)
    {
        Bank_Main bank_main=trie_tree.getBank(name);
        if (bank_main!=null) {
            if (bank_main.branches.root != null)
                bank_main.branches.print_all();
            else
                System.out.println("this bank has no branch");
            Stack_Node stack_node = new Stack_Node("listBrs", null, null, null, null);
            stack.push(stack_node);
        }

    }
    public static void nearB(Stack stack,KdTree kdTree,int []point)
    {
        if (kdTree.root!=null) {
            Node node = kdTree.nearest(point);
            if (node.bank_branch==null)
                System.out.println("main bank name: "+node.bank_main.Name+" coordinates:  x:"+node.coordinates[0]+" y:"+node.coordinates[1]);
            else
                System.out.println("main bank name: "+node.bank_main.Name+" branch name:"+node.bank_branch.Branch_Name+" coordinates:  x:"+node.coordinates[0]+" y:"+node.coordinates[1]);
            Stack_Node stack_node = new Stack_Node("nearB", null, null, null, null);
            stack.push(stack_node);
        }
        else
        {
            System.out.println("pleas enter a bank");
        }
    }
    public static void nearBr(Stack stack,Trie_Tree trie_tree,int[] point,String name)
    {
        Bank_Main bank_main=trie_tree.getBank(name);
        if (bank_main!=null) {
            Node node = bank_main.branches.nearest(point);
            System.out.println("main bank name: "+node.bank_main.Name+" branch name:"+node.bank_branch.Branch_Name+" coordinates:  x:"+node.coordinates[0]+" y:"+node.coordinates[1]);
            Stack_Node stack_node = new Stack_Node("nearBr", null, null, null, null);
            stack.push(stack_node);
        }
    }
    public static void availB(Stack stack,KdTree kdTree,int R,int []point)
    {
        if (kdTree.root!=null) {
            kdTree.circle(point, R);
            Stack_Node stack_node = new Stack_Node("availB", null, null, null, null);
            stack.push(stack_node);
        }
        else
            System.out.println("there are no banks");
    }
    public static void Undo(Stack stack,int p,KdTree kdTree,Trie_Tree main_banks,Trie_Tree neighborhoods)
    {
        Stack stack1=new Stack();
        if (p>stack.Stack_top)
        {
            System.out.println("there arent as many commends");
        }
        else
        {
            KdTree kdTree1=new KdTree();
            Trie_Tree main_banks1=new Trie_Tree();
            Trie_Tree  neighborhoods1=new Trie_Tree();
            while(stack.Stack_top>p)
            {
                Stack_Node stack_node=stack.pop();
                if (stack_node.cm.equals("addBr"))
                {
                    delBr(stack,kdTree,stack_node.bank_branch.coordinates);
                    stack.pop();
                }

            }
            while (!stack.empty())
            {
                stack1.push(stack.pop());
            }
            while (!stack1.empty())
            {
                Stack_Node stack_node= stack1.pop();
                if (stack_node.cm.equals("addN"))
                {
                    addN(stack,neighborhoods1,stack_node.neighborhood.name,stack_node.neighborhood.point1,stack_node.neighborhood.point2);
                }
                else if (stack_node.cm.equals("addB"))
                {
                    addB(stack,kdTree1,main_banks1,stack_node.bank_main);
                }
                else if (stack_node.cm.equals("addBr"))
                {
                    addBr(stack,kdTree1,main_banks1,stack_node.bank_main.Name,stack_node.bank_branch,true);
                }
                else if (stack_node.cm.charAt(0)=='d')
                {
                    delBr(stack,kdTree1,stack_node.coordinates);
                }
                else
                {
                    stack.push(stack_node);
                }
            }
            kdTree.root=kdTree1.root;
            kdTree.size= kdTree1.size;
            kdTree.delete_find=kdTree1.delete_find;
            main_banks.root=main_banks1.root;
            neighborhoods.root=neighborhoods1.root;
        }
    }
    public static int [] arraymaker()
    {
        Scanner scanner=new Scanner(System.in);
        int point_x;
        int point_y;
        while (true) {
            try {
                String point=scanner.nextLine();
                String[] arrOfpoint1 = point.split(" ");
                point_x = Integer.parseInt(arrOfpoint1[0]);
                point_y = Integer.parseInt(arrOfpoint1[1]);
                break;
            } catch (NumberFormatException e) {
                System.out.println("please enter integers in the format of [x y]");
            }
        }
        int [] points=new int[2];
        points[0]=point_x;points[1]=point_y;
        return points;
    }
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        KdTree kdTree=new KdTree();
        Trie_Tree main_banks=new Trie_Tree();
        Trie_Tree neighborhoods=new Trie_Tree();
        Stack stack=new Stack();
        /*Bank_Main bank_main1=new Bank_Main("bank1", new int[]{30, 40});addB(kdTree,main_banks,bank_main1);
        Bank_Branch bank_branch1=new Bank_Branch("bank1","bank_branch1", new int[]{5, 25});addBr(kdTree,main_banks,"bank1",bank_branch1);
        Bank_Branch bank_branch2=new Bank_Branch("bank1","bank_branch2", new int[]{70, 70});addBr(kdTree,main_banks,"bank1",bank_branch2);
        Bank_Branch bank_branch3=new Bank_Branch("bank1","bank_branch3", new int[]{10, 12});addBr(kdTree,main_banks,"bank1",bank_branch3);
        Bank_Branch bank_branch4=new Bank_Branch("bank1","bank_branch4", new int[]{50,30});addBr(kdTree,main_banks,"bank1",bank_branch4);
        Bank_Branch bank_branch5=new Bank_Branch("bank1","bank_branch5", new int[]{35, 45});addBr(kdTree,main_banks,"bank1",bank_branch5);
        Bank_Branch bank_branch6=new Bank_Branch("bank1","bank_branch6", new int[]{10, 2});addBr(kdTree,main_banks,"bank1",bank_branch6);
        delBr(kdTree, new int[]{70, 70});
        //ListBrs(main_banks,"bank1");
        kdTree.print_all();
        System.out.println();
        bank_main1.branches.print_all();
        nearBr(main_banks, new int[]{7, 8},"bank1");
        System.out.println();
        addN(neighborhoods,"neighborhood1", new int[]{0, 0}, new int[]{32, 42});
        addN(neighborhoods,"neighborhood2", new int[]{0, 0}, new int[]{50, 30});
        ListB(kdTree,neighborhoods,"neighborhood1");
        System.out.println();
        nearBr(main_banks, new int[]{30, 40},"bank1");
        System.out.println();
        availB(kdTree,30, new int[]{0, 0});*/
        String input;
        label:
        while (true)
        {
            input=scanner.nextLine();
            switch (input) {
                case "addN": {
                    System.out.println("enter the name of neighborhood");
                    String neighborhood_name = scanner.nextLine();
                    System.out.println("enter the bottom-left point coordinates");
                    int[] points1 = arraymaker();
                    System.out.println("enter the top-right point coordinates");
                    int[] points2 = arraymaker();
                    addN(stack,neighborhoods, neighborhood_name, points1, points2);
                    break;
                }
                case "addB": {
                    System.out.println("enter the name of the bank");
                    String name = scanner.nextLine();
                    System.out.println("enter the coordinates of the bank");
                    int[] coordinates = arraymaker();
                    Bank_Main bank_main = new Bank_Main(name, coordinates);
                    addB(stack,kdTree, main_banks, bank_main);
                    break;
                }
                case "addBr": {
                    System.out.println("enter the name of the bank");
                    String bank_name = scanner.nextLine();
                    System.out.println("enter the name of the branch");
                    String branch_name = scanner.nextLine();
                    System.out.println("enter the coordinates of the bank");
                    int[] coordinates = arraymaker();
                    Bank_Branch bank_branch = new Bank_Branch(bank_name, branch_name, coordinates);
                    addBr(stack,kdTree, main_banks, bank_name, bank_branch,false);
                    break;
                }
                case "delBr": {
                    System.out.println("enter the coordinates");
                    int[] coordinates = arraymaker();
                    delBr(stack,kdTree, coordinates);
                    break;
                }
                case "listB": {
                    System.out.println("enter the name of the neighborhood");
                    String neighborhood_name = scanner.nextLine();
                    ListB(stack,kdTree, neighborhoods, neighborhood_name);
                    break;
                }
                case "listBrs": {
                    System.out.println("enter the name of the bank");
                    String bank_name = scanner.nextLine();
                    ListBrs(stack,main_banks, bank_name);
                    break;
                }
                case "nearB": {
                    System.out.println("enter the coordinates");
                    int[] coordinates = arraymaker();
                    nearB(stack,kdTree, coordinates);
                    break;
                }
                case "nearBr": {
                    System.out.println("enter the name of the bank");
                    String bank_name = scanner.nextLine();
                    System.out.println("enter the coordinates");
                    int[] coordinates = arraymaker();
                    nearBr(stack,main_banks, coordinates, bank_name);
                    break;
                }
                case "availB": {
                    System.out.println("enter the coordinates of the center of the circle");
                    int[] coordinates = arraymaker();
                    System.out.println("enter the radius");
                    int R;
                    while (true) {
                        try {
                            String R1 = scanner.nextLine();
                            R = Integer.parseInt(R1);
                            break ;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("please enter valid integer");
                        }

                    }
                    availB(stack,kdTree, R, coordinates);
                    break;
                }
                case "undo":
                {
                    int P;
                    System.out.println("enter the number of the command");
                    while (true) {
                        try {
                            String P1 = scanner.nextLine();
                            P = Integer.parseInt(P1);
                            break ;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("please enter valid integer");
                        }

                    }
                    Undo(stack,P,kdTree,main_banks,neighborhoods);
                    break;
                }
                case "print":
                    kdTree.print_all();
                    break;
                case "exit":
                    break label;
            }
        }
    }
}
