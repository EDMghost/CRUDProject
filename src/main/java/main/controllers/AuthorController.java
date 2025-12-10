package main.controllers;

import main.entities.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import main.repos.AuthorRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("authors", authorRepo.findAll());
        return "authors";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("author", new Author());
        return "author-form";
    }

    @PostMapping
    public String save(@ModelAttribute Author author) {
        authorRepo.save(author);
        return "redirect:/authors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorRepo.findById(id).orElseThrow());
        return "author-form";
    }

    /*
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorRepo.deleteById(id);
        return "redirect:/authors";
    }
    */

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Author author = authorRepo.findById(id).orElseThrow();

        if (!author.getBooks().isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "A szerző nem törölhető, mert vannak hozzá tartozó könyvek!");
            return "redirect:/authors";
        }

        authorRepo.delete(author);
        return "redirect:/authors";
    }
}