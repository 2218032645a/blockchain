package cn.my.controller;

import cn.my.service.BlockChainManager;
import cn.my.service.NodeManager;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//区块链节点相关接口，提供节点注册，解决冲突等功能
/*区块链系统是分布式的，这是去中心化思想的精髓，也是去中心化社会运行的根本，
可以说区块链是一个运行在许多节点上的系统，每个节点上的链都要保证是一样的，
就如同人手一个相同的账本，丢失一两本对整个交易系统造不成多少影响。那么我们如何保证各个节点链的一致性呢？
就要实现一致性算法，使每个节点都注册其他所有节点，如果哪个节点有了更长的链，其他节点实时同步它*/
@RestController
@RequestMapping("node")
public class NodeController {
    @Autowired
    private NodeManager nodeManager;
    //注册节点
    @RequestMapping("register")
    public String nodeRegister(@RequestBody List<String> nodes) {
        if (nodes != null && nodes.size() > 0) {
            for (String url : nodes) {
                try {
                    nodeManager.register(url);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("message", "节点注册成功");
                    map.put("节点信息", nodeManager.getNodes());
                    Set<String> nodes1 = nodeManager.getNodes();
//                    System.out.println(nodes1);
                    return JSONObject.toJSONString(map, true);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

        }
        return "请添加节点";
    }

    //查看节点信息
    @RequestMapping("allNode")
    public String checkNode() {
        Set<String> nodes = nodeManager.getNodes();
        HashMap<String, Object> map = new HashMap<>();
        map.put("节点信息", nodes);
        return JSONObject.toJSONString(map, true);
    }
    //解决节点冲突。寻找所有节点中的最长链，找的到就将本节点的链替换，找的过程中验证链的有效性(节点共识)
    @RequestMapping("consensus")
    public String resolve() {
        //寻找最长链，找到返回true，否则返回false
        HashMap<String, Object> map = new HashMap<>();
        try {
            boolean find = nodeManager.seekLongestChain();
            if (find) {
                map.put("message", "已同步最长链");
                map.put("新链", nodeManager.getChain());
            } else {
                map.put("message", "本链为权威链");
                map.put("本节点链", nodeManager.getChain());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }
}
