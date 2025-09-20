package com.YouTubeTools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.YouTubeTools.model.VideoDetails;
import com.YouTubeTools.service.ThumbnailService;
import com.YouTubeTools.service.YouTubeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YouTubeVideoController {

    private final YouTubeService youTubeService;
    private final ThumbnailService service;

    @GetMapping("/video-details")
    public String showVideoForm() {
        return "video-details";
    }

    @PostMapping("/video-details")
    public String fetchVideoDetails(@RequestParam("videoUrlOrId") String videoUrlOrId, Model model) {
        
        // This line is the key change. Pass the original videoUrlOrId to the service.
       String videoId = service.extractVideoId(videoUrlOrId);
        
        if (videoId == null) {
            model.addAttribute("error", "Invalid youtube url, id");
            return "video-details";
        }
            
        VideoDetails details = youTubeService.getVideoDetails(videoId);
            
        if(details ==null) {
        	model.addAttribute("error", "video not found");
        }else {
            model.addAttribute("videoDetails", details);
        }

        model.addAttribute("videoUrlOrId", videoUrlOrId); // Ensure the input field value is preserved
        return "video-details";
    }
}