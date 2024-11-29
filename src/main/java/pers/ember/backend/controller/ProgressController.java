package pers.ember.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.ember.backend.dao.ProgressDao;
import pers.ember.backend.entity.Progress;
import pers.ember.backend.service.ProgressService;
import pers.ember.backend.vo.CommonResponse;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/get")
    public CommonResponse get(@RequestHeader("Authorization") String token) {
        return progressService.get(token);
    }

    @PostMapping("/insert")
    public CommonResponse insert(@RequestHeader("Authorization") String token, @RequestBody Progress progress) {
        return progressService.insert(token, progress);
    }
    @GetMapping("/delete")
    public CommonResponse delete(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        return progressService.delete(token, id);
    }
    @PostMapping("/icon")
    public CommonResponse icon(@RequestHeader("Authorization") String token,@RequestParam("file") MultipartFile file) {
        return progressService.icon(token, file);
    }
    @GetMapping("/deleteIcon")
    public CommonResponse deleteIcon(@RequestHeader("Authorization") String token,@RequestParam("icon") String icon) {
        return progressService.deleteIcon(token,icon);
    }
    @PostMapping("/update")
    public CommonResponse update(@RequestHeader("Authorization") String token,@RequestBody ProgressDao progressDao) {
        return progressService.update(token,progressDao);
    }

}
