package sample;

public class UnionFind {

    public static int findIterative(int[] a, int id) { //finds iteratively using int parameters.
        if (a[id]==-1) return -1;
        while(a[id]!=id) { //while a[id] is not equal to the int id....
            a[id]=a[a[id]]; //Compresses the set's path to involve less hops
            id=a[id];//id is set to equal a[id].
        }
        return id; //id, now having a new value, is returned.
    }

    public static int findRecursive(int[] a, int id) { //finds recursively using int parameters.
        if(a[id]==id) return id; //if a[id] is equal to the int id, id is returned.
        else return a[id]= findRecursive(a,a[id]); //otherwise, a[id] is equal to another find taking in parameters a, and a[id].
    }

    //Iterative version which is object oriented, using node parameters.
    public static DisjointNode<?> findIterativeObject(DisjointNode<?> node){
        while(node.parent!=null) { //while the node has a parent...
            if(node.parent.parent!=null) node.parent=node.parent.parent; //Compresses the set's path to involve less hops.
            node=node.parent; //the node is set to equal node parent.
        }
        return node; //node with new value is returned.
    }
    //Recursive version which is object oriented, using node parameters.
    public static DisjointNode<?> findRecursiveObject(DisjointNode<?> node){
        if(node.parent==null) return node;
        else return node.parent= findRecursiveObject(node.parent);
    }

    //Quick union using int parameters.
    public static void quickUnion(int[] a, int p, int q) {
        a[findIterative(a,q)]=findIterative(a,p); //The root q now points to the root p
    }

    //Quick union of disjoint sets containing elements p and q (Version 2)
    public static void quickUnionObject(DisjointNode<?> p, DisjointNode<?> q) {
        findRecursiveObject(q).parent=findRecursiveObject(p); //The root of q is made reference the root of p
    }

    //union by height (using node parameters)
    public static void unionByHeight(DisjointNode<?> p, DisjointNode<?> q) {
        DisjointNode<?> rootp=findRecursiveObject(p); //the root of p is equal to the result of finding node object p recursively.
        DisjointNode<?> rootq=findRecursiveObject(q); //the root of q is equal to the result of finding node object q recursively.
        DisjointNode<?> deeperRoot=rootp.height>=rootq.height ? rootp : rootq;
        //deeperRoot is equal to rootp's height being greater than rootq's height (using lambda statements)
        DisjointNode<?> shallowerRoot=deeperRoot==rootp ? rootq : rootp;
        shallowerRoot.parent=deeperRoot;
        //shalowerRoot's parent is set to equal deeperRoot.
        if(deeperRoot.height==shallowerRoot.height) deeperRoot.height++;
        //if deeperRoot's height equals shallowerRoot's height, deeperRoot's height increments.
    }

    //union by size (using node parameters)
    public static void unionBySize(DisjointNode<?> p, DisjointNode<?> q) {
        DisjointNode<?> rootp=findRecursiveObject(p); //the root of p is equal to the result of finding node object p recursively.
        DisjointNode<?> rootq=findRecursiveObject(q); //the root of q is equal to the result of finding node object q recursively.
        DisjointNode<?> biggerRoot=rootp.size>=rootq.size ? rootp : rootq;
        //biggerRoot is equal to rootp's size being greater than rootq's size (using lambda statements)
        DisjointNode<?> smallerRoot=biggerRoot==rootp ? rootq : rootp;
        smallerRoot.parent=biggerRoot;
        //smallerRoot's parent is set to equal biggerRoot.
        biggerRoot.size+=smallerRoot.size;
        //biggerRoot's size now += smallerRoot's size.
    }








}

