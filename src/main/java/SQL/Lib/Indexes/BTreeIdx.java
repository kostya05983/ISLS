package SQL.Lib.Indexes;


import java.nio.ByteBuffer;
import java.util.Arrays;

public class BTreeIdx {
    private int height;
    private Node[] nodes;
    private String[] keysExpresion;
    private int[] positions;
    private int currentAmount=0;


    BTreeIdx(String[] keysExpresion, int[] positions){
        nodes=new Node[keysExpresion.length];
        this.keysExpresion=keysExpresion;
        this.positions=positions;
    }

    public BTreeIdx(){
    }

    void init(){
        //Устанавливаем корень
        nodes[0]=new Node();
        nodes[0].attribute=(byte)0;
        nodes[0].keyAmount=1;
        nodes[0].left=-1;
        nodes[0].right=-1;
        nodes[0].setKey(keysExpresion[0]);
        //Устанавливаем индексную вершину
        nodes[1]=new Node();
        nodes[1].attribute=(byte)1;
        nodes[1].keyAmount=1;
        nodes[1].left=-1;
        nodes[1].right=-1;
        nodes[1].setKey(keysExpresion[1]);

        int amount=positions.length;
        //Устанавливаем листья
        for(int i=0;i<nodes.length;i++){
            nodes[2]=new Node();
            nodes[2].attribute=(byte)2;
            //Добавить set
            nodes[2].setKey(keysExpresion[i]);
            if(amount>62) {
                nodes[2].setKeys(Arrays.copyOfRange(positions, 62*i, 62 * (i+1)));
                amount-=62;
            }else{
                nodes[2].setKeys(Arrays.copyOfRange(positions,62*i,positions.length));
                amount=0;
            }
            nodes[2].left=-1;

            if(i!=(nodes.length-1))
            nodes[2].right=512*(2+(i+1))-1;
            else
                nodes[2].right=-1;

            if(amount==0)
                break;
        }
    }

    private class Node{
        private byte attribute;//Атрибут вершины
        private short keyAmount=0;//Колличество существующих ключей
        private int left;
        private int right;
        private byte[] data=new byte[500];

        private void setKeys(int[] positions){
            ByteBuffer byteBuffer=ByteBuffer.allocate(8);
            for(int i=0;i<positions.length;i++){
                byteBuffer.putInt(positions[i]);
                byteBuffer.putInt(BTreeIdx.this.currentAmount);
                System.arraycopy(byteBuffer.array(),0,data,i*8+1,8);
                byteBuffer.clear();
                keyAmount++;
            }

        }
        private void setKey(String key){
            System.arraycopy(key.getBytes(),0,data,0,key.getBytes().length);
            keyAmount++;
        }
    }

    public byte[] getByteCode(){
        ByteBuffer result=ByteBuffer.allocate(nodes.length*512);
        for (Node node : nodes) {
            result.put(node.attribute);
            result.putShort(node.keyAmount);
            result.putInt(node.left);
            result.putInt(node.right);
            result.put(node.data);
        }
        return result.array();
    }

    public void setByteCode(byte[] byteCode){
        ByteBuffer byteBuffer=ByteBuffer.wrap(byteCode);
        nodes=new Node[(byteCode.length+1)/512];

        for(int i=0;i<nodes.length;i++){
            nodes[i].attribute=byteBuffer.get(i*512);
            nodes[i].keyAmount=byteBuffer.getShort(i*512+1);
            nodes[i].left=byteBuffer.getInt(i*512+3);
            nodes[i].right=byteBuffer.getInt(i*512+7);
            byteBuffer.get(nodes[i].data,i*512+11,500);
        }
    }

}
