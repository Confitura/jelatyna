package pl.confitura.jelatyna.news;

import java.util.List;

import org.springframework.data.repository.Repository;

public interface NewsRepository extends Repository<News, String> {
    News save(News news);

    List<News> findAll();

    News findOne(String id);

    void delete(String id);
}
