package com.campusamour.inoteblog.web.admin;

import com.campusamour.inoteblog.model.Tag;
import com.campusamour.inoteblog.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, Model model) {
        // 使用PageHelper实现分页
        String orderBy = "id desc";
        PageHelper.startPage(pageNum, 10, orderBy);
        List<Tag> tagList = tagService.selectAllTags();
        PageInfo<Tag> pageInfo = new PageInfo<>(tagList);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/tags-admin";
    }

    @GetMapping("/tags/input")
    public String input() {
        return "admin/tags-input";
    }


    //  新增分类
    @PostMapping("/tags")
    public String add(Tag tag, RedirectAttributes attributes) {
        Tag temp_tag = tagService.searchTagByName(tag.getName());
        if (temp_tag != null) {
            attributes.addFlashAttribute("message", "不能添加重复的标签");
            return "redirect:/admin/tags/input";
        }
        int t = tagService.saveTag(tag);
        if (t == 0) {
            attributes.addFlashAttribute("message", "新增标签失败");
        } else {
            attributes.addFlashAttribute("message", "新增标签成功");
        }

        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.searchTagById(id));
        return "admin/tags-input";
    }

    @PostMapping("/tags/{id}")
    public String editTag(Tag tag, @PathVariable Long id, RedirectAttributes attributes) {
        Tag temp_tag = tagService.searchTagByName(tag.getName());
        if (temp_tag != null) {
            attributes.addFlashAttribute("message", "标签名称已存在");
            return "redirect:/admin/tags/" + id + "/input";
        }
        tag.setId(id);
        int t = tagService.updateTag(tag);
        if (t == 0) {
            attributes.addFlashAttribute("message", "标签更新失败");
        } else {
            attributes.addFlashAttribute("message", "标签更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        Tag temp_tag = tagService.searchTagById(id);
        if (temp_tag == null) {
            attributes.addFlashAttribute("message", "更新项不存在");
            return "redirect:/admin/tags/input";
        }
        tagService.removeTagById(id);
        attributes.addFlashAttribute("message", "标签移除成功");
        return "redirect:/admin/tags";
    }
}
