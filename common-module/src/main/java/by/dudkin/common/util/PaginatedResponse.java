package by.dudkin.common.util;

import by.dudkin.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alexander Dudkin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    public static <ID extends Serializable, E extends BaseEntity<ID>, T>
    PaginatedResponse<T> fromPage(Page<E> pageResult, List<T> content) {
        return new Builder<T>()
                .content(content)
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .build();
    }

    public static class Builder<T> {
        private List<T> content;
        private int page;
        private int size;
        private int totalPages;
        private long totalElements;

        public Builder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        public Builder<T> page(int page) {
            this.page = page;
            return this;
        }

        public Builder<T> size(int size) {
            this.size = size;
            return this;
        }

        public Builder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public PaginatedResponse<T> build() {
            PaginatedResponse<T> response = new PaginatedResponse<>();
            response.content = this.content;
            response.page = this.page;
            response.size = this.size;
            response.totalPages = this.totalPages;
            response.totalElements = this.totalElements;
            return response;
        }
    }

}
