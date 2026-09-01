package SmartBankAI.dto;

import java.util.List;

public class CustomerPageResponseDTO {
    private List<CustomerResponseDTO> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
    private boolean isLast;

    public CustomerPageResponseDTO() {
    }

    public CustomerPageResponseDTO(List<CustomerResponseDTO> content, int pageNumber, int totalPages, long totalElements, boolean isLast) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isLast = isLast;
    }

    public List<CustomerResponseDTO> getContent() {
        return content;
    }

    public void setContent(List<CustomerResponseDTO> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int воспа() {
        return totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}