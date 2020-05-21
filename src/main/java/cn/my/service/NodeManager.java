package cn.my.service;

import cn.my.domain.Block;
import cn.my.domain.BlockChain;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

//节点管理
@Service
public class NodeManager {
    private BlockChain blockChain = BlockChain.getInstance();
    public void register(String url) throws MalformedURLException {
        URL url1 = new URL(url);
        String node = url1.getHost() + ":" + url1.getPort();
        blockChain.getNodes().add(node);
    }

    public Set<String> getNodes() {
        return blockChain.getNodes();
    }

    public boolean seekLongestChain() throws IOException {
        //访问节点列表里的所有节点，寻找节点上的链并与本链进行长度比较
        Set<String> nodes = blockChain.getNodes();
        //定义一个最长链的长度，先设置成本节点的链的长度
        long maxLength = blockChain.getChain().size();
        //定义一个新链用于替换原有的短链,先设置成本节点的链
        List<Block> newChain = null;
        if (nodes != null && nodes.size() > 0) {
            for (String node : nodes) {
                URL url = new URL("http://" + node + "/chain");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    reader.close();
                    JSONObject jsonChain = JSON.parseObject(stringBuffer.toString());
                    long length = jsonChain.getLongValue("length");
                    List<Block> chain = jsonChain.getJSONArray("chain").toJavaList(Block.class);
//                    System.out.println(chain.toArray());
                    //验证链的有效性并比较链的长度
                    if (validChain(chain) && length > maxLength) {
                        maxLength = length;
                        newChain = chain;
                    }
                }
            }
        }
        //如果发现了一条新的有效的最长链，则替换为本节点的链
        if (newChain != null) {
            blockChain.setChain(newChain);
            return true;
        }
        return false;
    }

    private boolean validChain(List<Block> chain) {
        //前一个区块，先设置成第一个
        Block previousBlock = chain.get(0);
        for (int i = 1; i < chain.size(); i++) {
            if (!previousBlock.getHash().equals(chain.get(i).getPreviousHash())) {
                return false;
            }
            previousBlock = chain.get(i);
        }
        return true;
    }

    public List<Block> getChain() {
        return blockChain.getChain();
    }
}
