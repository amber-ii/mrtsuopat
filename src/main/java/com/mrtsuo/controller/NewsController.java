package com.mrtsuo.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mrtsuo.domain.News;
import com.mrtsuo.service.NewsService;

@Controller
@RequestMapping("/admin")
public class NewsController {

	/**
	 * 最新消息(後台)
	 */

	private static final String LIST = "admin/news";
	private static final String INPUT = "admin/news-input";
	private static final String REDIRECT_LIST = "redirect:/admin/news";

	@Autowired
	private NewsService newsService;

//	最新消息列表
	@GetMapping("/news")
	public String news(
			@PageableDefault(size = 5, sort = { "updateTime" }, direction = Sort.Direction.DESC) Pageable pageable,
			News news, Model model) {
		model.addAttribute("page", newsService.listNews(pageable, news));
		return LIST;
	}

//	關鍵字搜尋
	@PostMapping("/news/search")
	public String search(
			@PageableDefault(size = 5, sort = { "updateTime" }, direction = Sort.Direction.DESC) Pageable pageable,
			News news, Model model) {
		model.addAttribute("page", newsService.listNews(pageable, news));
		return "admin/news :: newsList";
	}

//	至新增頁面
	@GetMapping("/news/input")
	public String input(Model model) {
		model.addAttribute("news", new News());
		return INPUT;

	}

//	至編輯畫面
	@GetMapping("/news/{id}/input")
	public String editInput(@PathVariable Long id, Model model) {
		model.addAttribute("news", newsService.getNews(id));
		return INPUT;
	}

//	新增、修改
	@PostMapping("/news")
	public String post(News news, RedirectAttributes attributes) {
		News n;
		if (news.getId() == null) {
			n = newsService.saveNews(news);
		} else {
			n = newsService.updateNews(news.getId(), news);
		}
		if (n == null) {
			attributes.addFlashAttribute("message", "操作失敗");
		} else {
			attributes.addFlashAttribute("message", "操作成功");
		}
		return REDIRECT_LIST;
	}

//	刪除
	@GetMapping("/news/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes attributes) {
		newsService.deleteNews(id);
		attributes.addFlashAttribute("message", "刪除成功");
		return REDIRECT_LIST;
	}



// 選擇文件上傳，heroku不支援儲存，暫不用。 
	/**
	@PostMapping("/news")
	public String post(@RequestParam("img") MultipartFile multipartFile, News news, RedirectAttributes attributes)
			throws Exception {
		News n;
		if (news.getId() == null) {
			String oldFileName = multipartFile.getOriginalFilename();
			String newFileName = UUID.randomUUID() + oldFileName;
			File targetFile = new File(
					File.separator + "Users" + File.separator + "amber" + File.separator + "upload" + File.separator,
					newFileName);
			news.setPicture(newFileName);
			try {
				multipartFile.transferTo(targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			n = newsService.saveNews(news);
		} else

		{
			n = newsService.updateNews(news.getId(), news);
		}

		if (n == null) {
			attributes.addFlashAttribute("message", "操作失敗");
		} else {
			attributes.addFlashAttribute("message", "操作成功");
		}
		return REDIRECT_LIST;
	}
	*/
}
