package com.haozhi.item.web.controller;

import com.haozhi.common.constants.StatusCode;
import com.haozhi.common.dto.ResultDTO;
import com.haozhi.item.dao.InvitationMapper;
import com.haozhi.item.dto.NewDto;
import com.haozhi.item.pojo.Invitation;
import com.haozhi.item.pojo.User;
import com.haozhi.item.service.TeamService;
import com.haozhi.item.service.UserService;
import com.haozhi.item.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author kgy
 * @version 1.0
 * @date 2019/12/26 18:20
 */
@Controller
@RequestMapping("team")

public class TeamController extends BaseController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationMapper invitationMapper;

    @RequestMapping
    public String team(Map<String, Object> map) {

        NewDto newDto = teamService.queryNumPrice(getUser().getId());
        map.put("newDto", newDto);
        return "team/team";
    }

    /**
     * 查询业绩的页面跳转
     *
     * @return
     */
    @RequestMapping("query")
    public String queryBusiness() {
        return "team/team_query";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO searchList(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "date1", required = false) String date1,
            @RequestParam(name = "date2", required = false) String date2,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "20") Integer rows
    ) {
        List<User> list = teamService.searchList(type, name, date1,date2, getUser().getId(), page, rows);
        return new ResultDTO(true, StatusCode.OK, "查询成功", list);
    }

    @RequestMapping("new")
    public String Invitation(Map<String, Object> map) {
        map.put("superId",getUser().getId());
        List<Invitation> invitations = invitationMapper.selectAll();
        map.put("list",invitations.get(0));
        return "new";
    }

}
