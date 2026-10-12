import { useState } from "react";
import "./App.css";

const suggestions = [
  "Java programming",
  "Data structures",
  "Algorithms",
  "Information retrieval",
  "Search engines",
];

const features = [
  {
    icon: "ϟ",
    title: "Fast & Efficient",
    description: "Get the most relevant results in seconds.",
    color: "blue",
  },
  {
    icon: "◎",
    title: "Advanced Algorithms",
    description: "Includes exact, fuzzy, phrase, similarity and ranking search.",
    color: "green",
  },
  {
    icon: "▤",
    title: "Multiple Documents",
    description: "Search across 5 sample documents with ease.",
    color: "purple",
  },
  {
    icon: "♧",
    title: "Reliable & Accurate",
    description: "Powered by Java + Spring Boot and React.",
    color: "orange",
  },
];

function App() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [error, setError] = useState("");

  const searchDocuments = async (searchQuery = query) => {
    const cleanQuery = searchQuery.trim();

    if (!cleanQuery) {
      setResults([]);
      setSearched(false);
      setError("");
      return;
    }

    setQuery(cleanQuery);
    setLoading(true);
    setSearched(true);
    setError("");

    try {
      const response = await fetch(
        `http://localhost:8080/api/search/scored?q=${encodeURIComponent(
          cleanQuery
        )}`
      );

      if (!response.ok) {
        throw new Error("The search request failed.");
      }

      const data = await response.json();
      setResults(data);
    } catch (err) {
      console.error("Search error:", err);
      setResults([]);
      setError(
        "Unable to connect to the search service. Please check that the Java backend is running."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    searchDocuments();
  };

  const chooseSuggestion = (suggestion) => {
    setQuery(suggestion);
    searchDocuments(suggestion);
  };

  const scrollTo = (sectionId) => {
    document.getElementById(sectionId)?.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  };

  return (
    <div className="app" id="home">
      {/* Navigation */}
      <header className="header">
        <a
          className="brand"
          href="#home"
          aria-label="MiniSearch home"
          onClick={() => {
            setSearched(false);
            setResults([]);
          }}
        >
          <span className="brand-icon">⌕</span>
          <span className="brand-name">
            Mini<span>Search</span>
          </span>
          <span className="brand-divider" />
          <span className="brand-description">
            Information Retrieval System
          </span>
        </a>

        <nav className="navigation">
          <a
            href="#home"
            className="nav-link active"
            onClick={() => {
              setSearched(false);
              setResults([]);
            }}
          >
            <span>⌂</span> Home
          </a>
          <a
            href="#about"
            className="nav-link"
            onClick={() => scrollTo("about")}
          >
            About
          </a>
          <a
            href="#features"
            className="nav-link"
            onClick={() => scrollTo("features")}
          >
            Docs
          </a>
        </nav>
      </header>

      {/* Hero */}
      <main className="main">
        <section className="hero" aria-labelledby="hero-title">
          <div className="hero-symbol">
            <span className="hero-glass">⌕</span>
            <span className="spark spark-left">✦</span>
            <span className="spark spark-right">✦</span>
          </div>

          <div className="eyebrow">
            <span className="status-dot" />
            YOUR SMART DOCUMENT SEARCH
          </div>

          <h1 id="hero-title">
            Search Smarter,
            <br />
            <span>Find Faster</span>
          </h1>

          <p className="hero-description" id="about">
            Mini Search is a powerful information retrieval system that helps
            you find the most relevant information from your documents.
          </p>

          {/* Search form */}
          <form className="search-box" onSubmit={handleSubmit}>
            <span className="search-input-icon" aria-hidden="true">
              ⌕
            </span>

            <input
              type="search"
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Enter your search query..."
              aria-label="Search documents"
            />

            {query && (
              <button
                className="clear-button"
                type="button"
                aria-label="Clear search"
                onClick={() => {
                  setQuery("");
                  setResults([]);
                  setSearched(false);
                  setError("");
                }}
              >
                ×
              </button>
            )}

            <button className="search-button" type="submit" disabled={loading}>
              {loading ? "Searching..." : "Search"}
              {!loading && <span aria-hidden="true">→</span>}
            </button>
          </form>

          {/* Search suggestions */}
          <div className="suggestions">
            <span className="suggestion-spark">✧</span>
            <span className="suggestion-label">Try searching for:</span>

            {suggestions.map((suggestion) => (
              <button
                className="suggestion-chip"
                key={suggestion}
                type="button"
                onClick={() => chooseSuggestion(suggestion)}
              >
                {suggestion}
              </button>
            ))}
          </div>
        </section>

        {/* Search results */}
        {searched && (
          <section className="results-section" aria-live="polite">
            {loading ? (
              <p className="search-message">Searching your documents...</p>
            ) : error ? (
              <p className="search-message error-message">{error}</p>
            ) : results.length === 0 ? (
              <p className="search-message">
                No matching documents found for "{query}". Try another keyword.
              </p>
            ) : (
              <>
                <div className="results-heading">
                  <div>
                    <span className="section-kicker">YOUR SEARCH RESULTS</span>
                    <h2>Relevant documents</h2>
                  </div>
                  <span className="result-count">
                    {results.length} result{results.length === 1 ? "" : "s"}
                  </span>
                </div>

                {results.map((item) => {
                  const document = item.document || item;

                  return (
                    <article className="result-card" key={document.id}>
                      <div className="document-icon">▤</div>

                      <div className="document-details">
                        <h3>{document.title}</h3>
                        <p>{document.content}</p>
                        <span className="document-id">
                          Document ID: {document.id}
                        </span>
                      </div>

                      <div className="relevance">
                        <span>Relevance score</span>
                        <strong>
                          {item.score !== undefined
                            ? Number(item.score).toFixed(2)
                            : "N/A"}
                        </strong>
                      </div>
                    </article>
                  );
                })}
              </>
            )}
          </section>
        )}

        {/* Feature cards */}
        <section className="features-section" id="features">
          <div className="features-grid">
            {features.map((feature) => (
              <article className="feature-card" key={feature.title}>
                <div className={`feature-icon ${feature.color}`}>
                  <span>{feature.icon}</span>
                </div>

                <h2>{feature.title}</h2>
                <p>{feature.description}</p>
              </article>
            ))}
          </div>
        </section>

        {/* Footer */}
        <footer className="footer">
          <span className="footer-line" />
          <span>Mini Search Engine</span>
          <span className="footer-line" />
        </footer>
      </main>
    </div>
  );
}

export default App;
