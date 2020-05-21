package cn.my.utils;
//计算工作量证明，返回一个Proof
/*让我们来实现一个PoW算法，规则是：
寻找一个数 p，使得它与前一个区块的 proof 拼接成的字符串的 Hash 值以 4 个零开头：*/
public class ProofOfWork {
    public static long getProof(long lastProof) {
        long proof = 0;
        while (!validProof(proof, lastProof)) {
            proof += 1;
        }
        return proof;
    }

    private static boolean validProof(long proof, long lastProof) {
        String s = lastProof + "" + proof;
        return ComputeHash.getSHA256(s).startsWith("0000");
    }
}
