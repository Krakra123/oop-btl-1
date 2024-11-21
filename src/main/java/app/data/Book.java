package app.data;

import java.util.List;

public class Book {
    
	private String id;
	private String title;
	private String author;
    public String name;
    public String publisher;
    public String genre;
    public int yearPublished;
    private List<String> authors;
    boolean isBorrowed = false;
    
    public Book(String name, String publisher) {
        this.name = name;
        this.publisher = publisher;
    }
    
    public Book(String name, String publisher, String genre, int yearPublished) {
        this.name = name;
        this.publisher = publisher;
        this.genre = genre;
        this.yearPublished = yearPublished;
    }

	public class VolumeInfo {
		public class ImageLinks {
			public String thumbnail;
		}

		public String title;
		public List<String> authors;
		public String publisher;
		public String publishedDate;
		public String description;
		public String categories;
		public int pageCount;
		public ImageLinks imageLinks;
		public String previewLink;
		public String infoLink;
	}

	public Book(String title, List<String> authors, String publisher) {
		this.title = title;
		this.authors = authors;
		this.publisher = publisher;
		
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	public boolean isBorrowed() {
		return isBorrowed;
	}

	public void setBorrowed(boolean isBorrowed) {
		this.isBorrowed = isBorrowed;
	}
	
	@Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", yearPublished=" + yearPublished +
                	
                '}';
    }
}
