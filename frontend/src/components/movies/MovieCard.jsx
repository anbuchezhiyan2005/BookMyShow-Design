import { useNavigate } from 'react-router-dom';
import './MovieCard.css';

const MovieCard = ({ movie }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/movie/${movie.movieId}`);
  };

  return (
    <div className="movie-card" onClick={handleClick}>
      <div className="movie-poster">
        <div className="movie-icon">🎬</div>
      </div>
      <div className="movie-info">
        <h3 className="movie-title">{movie.title}</h3>
        <div className="movie-meta">
          <span className="movie-genre">{movie.genre}</span>
          <span className="movie-duration">{movie.duration} min</span>
        </div>
        <div className="movie-language">{movie.language}</div>
      </div>
    </div>
  );
};

export default MovieCard;
