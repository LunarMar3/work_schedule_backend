package pers.ember.backend.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.ember.backend.dao.ProgressDao;
import pers.ember.backend.entity.Progress;
import pers.ember.backend.vo.CommonResponse;
import pers.ember.backend.util.JWTUtil;
import pers.ember.backend.mapper.ProgressMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressService {

    @Autowired
    private ProgressMapper progressMapper;

    public CommonResponse get(String token) {
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            return new CommonResponse(401, "Unauthorized", null);
        }
        QueryWrapper<Progress> queryWrapper = new QueryWrapper<Progress>();
        queryWrapper.eq("email", claims.getSubject());
        List<Progress> progressList = progressMapper.selectList(queryWrapper);
        List<ProgressDao> progressStringList = new ArrayList<>();
        for (Progress progress : progressList) {
            progressStringList.add(new ProgressDao(progress.getId(), progress.getProgress()));
        }
        return new CommonResponse(200, "获取成功", progressStringList);
    }

    public CommonResponse insert(String token, Progress progress) {
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            return new CommonResponse(401, "Unauthorized", null);
        }
        progress.setUserEmail(claims.getSubject());
        progressMapper.insert(progress);
        return new CommonResponse(200, "插入成功", null);
    }

    public CommonResponse icon(String token, MultipartFile file) {
        String contentType = file.getContentType();
        if(!(contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png")))){
            return new CommonResponse(500, "图片格式错误", null);
        }
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            return new CommonResponse(401, "Unauthorized", null);
        }
        String saveDirectory = "/usr/android/icons";
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return new CommonResponse(500, "文件名称无效", null);
            }
            File saveFile = new File(directory, originalFilename);
            file.transferTo(saveFile);
            return new CommonResponse(200, "上传成功", originalFilename);

        } catch (Exception e) {
            return new CommonResponse(500, "上传失败", null);
        }
    }

    public CommonResponse deleteIcon(String token,String icon) {
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            return new CommonResponse(401, "Unauthorized", null);
        }
        String saveDirectory = "/usr/android/icons";
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, icon);
        if (file.exists()) {
            file.delete();
            return new CommonResponse(200, "删除成功", null);
        } else {
            return new CommonResponse(500, "文件不存在", null);
        }
    }

    public CommonResponse delete(String token, String id) {
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            return new CommonResponse(401, "Unauthorized", null);
        }
        QueryWrapper<Progress> queryWrapper = new QueryWrapper<Progress>();
        queryWrapper.eq("email", claims.getSubject());
        queryWrapper.eq("id", id);
        progressMapper.delete(queryWrapper);
        return new CommonResponse(200, "删除成功", null);
    }

    public CommonResponse update(String token, ProgressDao progressDao) {
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            return new CommonResponse(401, "Unauthorized", null);
        }
        QueryWrapper<Progress> queryWrapper = new QueryWrapper<Progress>();
        queryWrapper.eq("email", claims.getSubject());
        queryWrapper.eq("id", progressDao.getId());
        Progress progress = progressMapper.selectOne(queryWrapper);
        if (progress == null) {
            return new CommonResponse(500, "进度不存在", null);
        }
        progress.setProgress(progressDao.getProgress());
        progressMapper.update(progress, queryWrapper);
        return new CommonResponse(200, "更新成功", null);
    }
}
