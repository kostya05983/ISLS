package SQL.Lib.Indexes;


import SQL.Lib.Dbf.DataDbf;

class BTreeIdx {
    //private Node root;
    private int height;
    private Node[] nodes;
    private String[] keys;

    BTreeIdx(String[] keys, DataDbf dataDbf){
        nodes=new Node[keys.length];
        this.keys=keys;
    }
    void init(){
        //Устанавливаем корень
        nodes[0]=new Node();
        nodes[0].attribute=(byte)0;
        nodes[0].keyAmount=1;
        nodes[0].left=-1;
        nodes[0].right=-1;
        nodes[0].setKey(keys[0]);
        //Устанавливаем индексную вершину
        nodes[1]=new Node();
        nodes[1].attribute=(byte)1;
        nodes[1].keyAmount=1;
        nodes[1].left=-1;
        nodes[1].right=-1;
        nodes[1].setKey(keys[1]);
        //Устанавливаем листья
        for(int i=0;i<nodes.length;i++){

        }
    }

    private static class Node{
        private byte attribute;//Атрибут вершины
        private short keyAmount;//Колличество существующих ключей
        private int left;
        private int right;
        private byte[] data=new byte[500];

        private void setKeys(String[] keys){

        }
        private void setKey(String key){
            System.arraycopy(key.getBytes(),0,data,0,key.getBytes().length);
        }
    }

}
