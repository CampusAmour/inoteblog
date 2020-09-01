package com.campusamour.inoteblog.web.admin;

import com.campusamour.inoteblog.model.Type;
import com.campusamour.inoteblog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, Model model) {

        // 使用PageHelper实现分页
        String orderBy = "id desc";
        PageHelper.startPage(pageNum,10,orderBy);
        List<Type> typeList = typeService.selectAllTypes();
        PageInfo<Type> pageInfo = new PageInfo<>(typeList);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/types-admin";
    }

    @GetMapping("/types/input")
    public String input() {
        return "admin/types-input";
    }


    //  新增分类
    @PostMapping("/types")
    public String add(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type temp_type = typeService.searchTypeByName(type.getName());
        if (temp_type != null) {
            attributes.addFlashAttribute("message", "不能添加重复的分类");
            return "redirect:/admin/types/input";
        }
        int t = typeService.saveType(type);
        if (t == 0) {
            attributes.addFlashAttribute("message", "新增分类失败");
        } else {
            attributes.addFlashAttribute("message", "新增分类成功");
        }

        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.searchTypeById(id));
        return "admin/types-input";
    }

    @PostMapping("/types/{id}")
    public String editType(Type type, @PathVariable Long id, RedirectAttributes attributes) {
        Type temp_type = typeService.searchTypeByName(type.getName());
        if (temp_type != null) {
            attributes.addFlashAttribute("message", "分类名称已存在");
            return "redirect:/admin/types/" + id + "/input";
        }
        type.setId(id);
        int t = typeService.updateType(type);
        if (t == 0) {
            attributes.addFlashAttribute("message", "更新分类失败");
        } else {
            attributes.addFlashAttribute("message", "更新分类成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        Type temp_type = typeService.searchTypeById(id);
        if (temp_type == null) {
            attributes.addFlashAttribute("message", "更新项不存在");
            return "redirect:/admin/types/input";
        }
        typeService.removeTypeById(id);
        attributes.addFlashAttribute("message", "移除成功");
        return "redirect:/admin/types";

    }
}
