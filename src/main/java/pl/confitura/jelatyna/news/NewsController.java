package pl.confitura.jelatyna.news;

import static java.net.URI.create;

import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/news")
public class NewsController {
    private NewsRepository repository;

    @Autowired
    public NewsController(NewsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> save(@RequestBody News news) throws Exception {
        deleteIfUpdate(news);
        news.setId(URLEncoder.encode(news.getTitle(), "UTF-8"));
        news = repository.save(news);
        return ResponseEntity.created(create("/news/" + news.getId())).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<News> find(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "" + Integer.MAX_VALUE) int limit) {
        return repository.findAll().stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public News findById(@PathVariable String id) {
        return repository.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        repository.delete(id);
        return ResponseEntity.ok().build();
    }

    private void deleteIfUpdate(@RequestBody News news) {
        if (news.getId() != null) {
            repository.delete(news.getId());
        }
    }

}
