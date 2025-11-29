package main.controllers;

import main.entities.Author;
import main.entities.Book;
import main.repos.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import main.repos.BookRepository;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookRepo.findAll());
        return "books";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepo.findAll());
        return "book-form";
    }

    @PostMapping
    public String save(@ModelAttribute Book book) {
        if (book.getAuthor() != null && book.getAuthor().getId() != null) {
            Author author = authorRepo.findById(book.getAuthor().getId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            book.setAuthor(author);  // beállítjuk a teljes Author objektumot
        }
        bookRepo.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookRepo.findById(id).orElseThrow();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorRepo.findAll());
        return "book-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookRepo.deleteById(id);
        return "redirect:/books";
    }
}