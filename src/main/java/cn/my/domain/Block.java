package cn.my.domain;

import cn.my.utils.ComputeHash;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

//定义一个区块类
public class Block {
    private long index;
    //设置区块哈希值
    private String hash;
    //设置前一个区块哈希值
    private String previousHash;
    //设置区块创建时间
    private String timeStamp;
    //设置工作量证明
    private long proof;
    //设置交易信息
    private List<Transaction> transactions;

    public Block() {
    }

    public Block(long proof, String previousHash) {
        this.previousHash = previousHash;
        this.proof = proof;
    }

    public long getProof() {
        return proof;
    }


    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getHash() {
        return hash;
    }


    public String getPreviousHash() {
        return previousHash;
    }


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setProof(long proof) {
        this.proof = proof;
    }
}
