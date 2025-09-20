package com.YouTubeTools.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.YouTubeTools.model.SearchVideo;
import com.YouTubeTools.model.Video;
import com.YouTubeTools.model.VideoDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class YouTubeService {

	private final WebClient.Builder webClientBuilder;

	@Value("${youtube.api.key}")
	private String apiKey;

	@Value("${youtube.api.base.url}")
	private String baseUrl;

	@Value("${youtube.api.max.related.videos}")
	private int maxRelatedVideos;

	public SearchVideo searchVideos(String videoTitle) {
		List<String> videoIds = searchForVideoIds(videoTitle);
		if (videoIds.isEmpty()) {
			return SearchVideo.builder()
					.primaryVideo(null)
					.relatedVideos(Collections.emptyList())
					.build();
		}

		String primaryVideoId = videoIds.get(0);
		List<String> relatedVideoIds = videoIds.subList(1, Math.min(videoIds.size(), maxRelatedVideos + 1));

		VideoDetails primaryVideoDetails = getVideoDetails(primaryVideoId);
		Video primaryVideo = (primaryVideoDetails != null) ? convertToVideo(primaryVideoDetails) : null;

		List<Video> relatedVideos = new ArrayList<>();
		for (String id : relatedVideoIds) {
			VideoDetails details = getVideoDetails(id);
			if (details != null) {
				relatedVideos.add(convertToVideo(details));
			}
		}

		return SearchVideo.builder()
				.primaryVideo(primaryVideo)
				.relatedVideos(relatedVideos)
				.build();
	}

	private List<String> searchForVideoIds(String videoTitle) {
		SearchApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
				.get()
				.uri(uriBuilder -> uriBuilder
						.path("/search")
						.queryParam("part", "snippet")
						.queryParam("q", videoTitle)
						.queryParam("type", "video")
						.queryParam("maxResults", maxRelatedVideos + 1)
						.queryParam("key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(SearchApiResponse.class)
				.block();

		if (response == null || response.items == null) {
			return Collections.emptyList();
		}

		List<String> videoIds = new ArrayList<>();
		for (SearchItem item : response.items) {
			if (item.id != null && item.id.videoId != null) {
				videoIds.add(item.id.videoId);
			}
		}
		return videoIds;
	}

	public VideoDetails getVideoDetails(String videoId) {
		VideoApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
				.get()
				.uri(uriBuilder -> uriBuilder
						.path("/videos")
						.queryParam("part", "snippet")
						.queryParam("id", videoId)
						.queryParam("key", apiKey)
						.build())
				.retrieve()
				.bodyToMono(VideoApiResponse.class)
				.block();

		if (response == null || response.items == null || response.items.isEmpty()) {
			return null;
		}

		Snippet snippet = response.items.get(0).snippet;

		if (snippet == null) {
			return null;
		}

		String thumbnailUrl = (snippet.thumbnails != null) ? snippet.thumbnails.getBestThumbnailUrl() : null;

		return VideoDetails.builder()
				.id(videoId)
				.channelTitle(snippet.channelTitle)
				.title(snippet.title)
				.description(snippet.description) // BUG FIX: Correctly maps the description
				.tags(Optional.ofNullable(snippet.tags).orElse(Collections.emptyList()))
				.thumbnailUrl(thumbnailUrl)
				.publishedAt(snippet.publishedAt)
				.build();
	}

	private Video convertToVideo(VideoDetails details) {
		return Video.builder()
				.id(details.getId())
				.channelTitle(details.getChannelTitle()) // Corrected field name
				.title(details.getTitle())
				.description(details.getDescription())
				.tags(details.getTags())
				.build();
	}
	
	// --- API Response Classes for JSON Deserialization ---

	@Data
	static class SearchApiResponse {
		List<SearchItem> items;
	}

	@Data
	static class SearchItem {
		Id id;
	}

	@Data
	static class Id {
		String videoId;
	}

	@Data
	static class VideoApiResponse {
		List<VideoItem> items;
	}

	@Data
	static class VideoItem {
		Snippet snippet;
	}

	@Data
	static class Snippet {
		String title;
		String description;
		String channelTitle;
		String publishedAt;
		List<String> tags;
		Thumbnails thumbnails;
	}

	@Data
	static class Thumbnails {
		Thumbnail maxres;
		Thumbnail high;
		Thumbnail medium;
		Thumbnail standard;
		Thumbnail _default; 
		
		String getBestThumbnailUrl() {
			if (maxres != null) return maxres.getUrl();
			if (high != null) return high.getUrl();
			if (standard != null) return standard.getUrl();
			if (medium != null) return medium.getUrl();
			return (_default != null) ? _default.getUrl() : "";
		}
	}

	@Data
	static class Thumbnail {
		String url;
		int width;
		int height;
	}
}