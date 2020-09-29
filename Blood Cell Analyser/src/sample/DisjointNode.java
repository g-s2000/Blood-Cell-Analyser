package sample;

public class DisjointNode<T>{
        public DisjointNode<?> parent=null; //parent is set to null by default.
        public T data; //data is generic, can have any value, e.g int, string
    public int size=1,height=1; //size and height values set to 1 by default
    public DisjointNode(T data) {
            this.data=data;//data is set
            this.size=size;//size is set
            this.height=height;//height is set
        }

    public T getData() { //data getter method
        return data;
    }

    public void setData(T data) { //data setter method
        this.data = data;
    }

    public DisjointNode<?> getParent() { //parent getter method
        return parent;
    }

    public void setParent(DisjointNode<?> parent) { //parent setter method
        this.parent = parent;
    }

    public int getSize() { //returns size of node.
        return size;
    }

    public void setSize(int size) { //sets node size.
        this.size = size;
    }

    public int getHeight() { //returns height of node.
        return height;
    }

    public void setHeight(int height) { //sets not height.
        this.height = height;
    }

    @Override
    public String toString() { //tostring method
        return "DisjointNode{" +
                "parent=" + parent +
                ", data=" + data +
                ", size=" + size +
                ", height=" + height +
                '}';
    }

    }

