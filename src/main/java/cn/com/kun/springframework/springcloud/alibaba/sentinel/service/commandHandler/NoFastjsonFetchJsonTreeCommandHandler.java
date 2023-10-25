package cn.com.kun.springframework.springcloud.alibaba.sentinel.service.commandHandler;

import cn.com.kun.common.utils.JacksonUtils;
import com.alibaba.csp.sentinel.Constants;
import com.alibaba.csp.sentinel.command.CommandHandler;
import com.alibaba.csp.sentinel.command.CommandRequest;
import com.alibaba.csp.sentinel.command.CommandResponse;
import com.alibaba.csp.sentinel.command.annotation.CommandMapping;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/10/23
 * desc:
*/
@CommandMapping(name = "noFastjsonJsonTree", desc = "get tree node VO start from root node")
public class NoFastjsonFetchJsonTreeCommandHandler implements CommandHandler<String> {

    @Override
    public CommandResponse<String> handle(CommandRequest request) {
        List<NodeVo> results = new ArrayList<NodeVo>();
        visit(Constants.ROOT, results, null);
        return CommandResponse.ofSuccess(JacksonUtils.toJSONString(results));
    }

    /**
     * Preorder traversal.
     */
    private void visit(DefaultNode node, List<NodeVo> results, String parentId) {
        NodeVo vo = NodeVo.fromDefaultNode(node, parentId);
        results.add(vo);
        String id = vo.getId();
        for (Node n : node.getChildList()) {
            visit((DefaultNode)n, results, id);
        }
    }
}
