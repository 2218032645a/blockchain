package cn.my.service;

import cn.my.domain.Block;
import cn.my.domain.BlockChain;
import cn.my.domain.Transaction;
import cn.my.utils.ComputeHash;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//区块链管理服务
@Service
public class BlockChainManager {
    private BlockChain blockChain = BlockChain.getInstance();
    //查看链
    public List<Block> getChain() {
        return blockChain.getChain();
    }
    //新增交易,返回新块的索引
    public long addTransaction(Transaction transaction) {
        blockChain.getTransactions().add(transaction);
        return blockChain.getChain().size()+1;
    }

    public List<Transaction> getTransactions() {
        return blockChain.getTransactions();
    }
    //获得最后一个区块
    public Block getLastBlock() {
        return blockChain.getChain().get(getChain().size() - 1);
    }

    public Block newBlock(long proof, String previousHash) {
        Block block = new Block(proof,previousHash);
        block.setTimeStamp(new Date().toString());
        block.setIndex(blockChain.getChain().size()+1);
        block.setTransactions(blockChain.getTransactions());
        block.setHash(ComputeHash.getSHA256(JSONObject.toJSONString(block)));
        //重置链上交易信息
        blockChain.setTransactions(new ArrayList<Transaction>());
        //将新块上链
        blockChain.getChain().add(block);
        return block;
    }
}
