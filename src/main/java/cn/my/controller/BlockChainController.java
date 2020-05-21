package cn.my.controller;

import cn.my.domain.Block;
import cn.my.domain.BlockChain;
import cn.my.domain.Transaction;
import cn.my.service.BlockChainManager;
import cn.my.utils.ProofOfWork;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
//区块链相关接口，提供查看链信息，新增交易，挖矿出块等功能
@RestController
public class BlockChainController {
    @Autowired
    private BlockChainManager blockChainManager;

    //查看当前链的信息
    @RequestMapping("chain")
    public String checkChain() {
        List<Block> chain = blockChainManager.getChain();
//        String jsonString = JSONObject.toJSONString(chain);
        HashMap<String, Object> map = new HashMap<>();
        map.put("chain", chain);
        map.put("length", chain.size());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    //新增交易
    @RequestMapping(value = "transaction/add", produces = "text/html;charset=UTF-8")
    public String addTransaction(@RequestBody Transaction transaction) {
        long index = blockChainManager.addTransaction(transaction);
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "新增交易成功,新的交易将添加到索引为" + index + "的区块中");
        map.put("transactions", blockChainManager.getTransactions());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    //挖矿，计算工作量证明并生成新块
    @RequestMapping(value = "mine")
    public String mine(HttpServletRequest request) {
        //获得最后一个区块的proof
        Block lastBlock = blockChainManager.getLastBlock();
        long lastProof = lastBlock.getProof();
        //计算工作量证明，获得新的证明
        long proof = ProofOfWork.getProof(lastProof);
        //给挖矿节点提供交易奖励，发送者为“0”表明是新挖出的币
        String nodeID = (String) request.getSession().getServletContext().getAttribute("nodeID");
        Transaction transaction = new Transaction();
        transaction.setSender("0");
        transaction.setReceiver(nodeID);
        transaction.setAmount(1);
        blockChainManager.addTransaction(transaction);
        //创建新块
        Block block = blockChainManager.newBlock(proof, lastBlock.getHash());
        //向客户端展示
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "新的区块已产生");
        map.put("区块排位", block.getIndex());
        map.put("交易信息", block.getTransactions());
        map.put("工作量证明", block.getProof());
        map.put("Hash", block.getHash());
        map.put("上一个区块Hash", block.getPreviousHash());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }
}
