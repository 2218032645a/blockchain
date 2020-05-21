package cn.my.domain;

import cn.my.utils.ComputeHash;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

//定义一个区块链
public class BlockChain {
    //其实就是把区块存储在一个集合中
    private List<Block> chain;
    //出块前当前链上的交易信息
    private List<Transaction> transactions;
    //链上所有节点信息
    private Set<String> nodes;
    private static BlockChain blockChain;

    //区块链明显只有一个实例，把区块链创建对象设置为单例模式
    private BlockChain() {
        this.chain = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.nodes = new HashSet<>();
        //创世区块
        getChain().add(genesis());
    }

    private Block genesis() {
        Block block = new Block(100, "0");
        block.setIndex(1);
        block.setTimeStamp(new Date().toString());
        block.setHash(ComputeHash.getSHA256(JSONObject.toJSONString(block)));
        return block;
    }

    public static BlockChain getInstance() {
        if (blockChain == null) {
            synchronized (BlockChain.class) {
                if (blockChain == null) {
                    blockChain = new BlockChain();
                }
            }
        }
        return blockChain;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    public static BlockChain getBlockChain() {
        return blockChain;
    }

    public static void setBlockChain(BlockChain blockChain) {
        BlockChain.blockChain = blockChain;
    }
}
