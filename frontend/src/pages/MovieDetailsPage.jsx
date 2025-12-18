import { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { movieApi } from '../api/movieApi';
import { theatreApi } from '../api/theatreApi';
import { showApi } from '../api/showApi';
import { BookingContext } from '../context/BookingContext';
import './MovieDetailsPage.css';

const MovieDetailsPage = () => {
  const { movieId } = useParams();
  const navigate = useNavigate();
  const { setSelectedMovie, setSelectedTheatre, setSelectedShow } = useContext(BookingContext);
  
  const [movie, setMovie] = useState(null);
  const [theatres, setTheatres] = useState([]);
  const [shows, setShows] = useState([]);
  const [selectedTheatreId, setSelectedTheatreId] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, [movieId]);

  const loadData = async () => {
    try {
      const movieData = await movieApi.getMovieById(movieId);
      setMovie(movieData);
      setSelectedMovie(movieData);

      const theatreData = await theatreApi.getTheatresByMovie(movieId);
      setTheatres(theatreData);

      const showData = await showApi.getShowsByMovie(movieId);
      setShows(showData);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getTheatreShows = (theatreId) => {
    return shows.filter(s => s.theatreId === theatreId);
  };

  const handleShowSelect = (show, theatre) => {
    setSelectedShow(show);
    setSelectedTheatre(theatre);
    navigate(`/seats/${show.showId}`);
  };

  if (loading) return <div className="page-container">Loading...</div>;
  if (!movie) return <div className="page-container">Movie not found</div>;

  return (
    <div className="page-container">
      <div className="movie-details">
        <div className="movie-poster-large">
          <div className="movie-icon-large">🎬</div>
        </div>
        
        <div className="movie-info-large">
          <h1>{movie.title}</h1>
          <div className="movie-meta-large">
            <span className="badge">{movie.genre}</span>
            <span>{movie.duration} minutes</span>
            <span>{movie.language}</span>
          </div>
          <p className="movie-description">{movie.description}</p>
          <p className="movie-release">Release Date: {new Date(movie.releaseDate).toLocaleDateString()}</p>
        </div>
      </div>

      <div className="theatres-section">
        <h2>Select Theatre & Show Time</h2>
        
        {theatres.map(theatre => {
          const theatreShows = getTheatreShows(theatre.theatreId);
          if (theatreShows.length === 0) return null;

          return (
            <div key={theatre.theatreId} className="theatre-card-large">
              <div className="theatre-info">
                <h3>{theatre.name}</h3>
                <p>{theatre.location}, {theatre.city}</p>
              </div>
              
              <div className="show-times">
                {theatreShows.map(show => (
                  <button
                    key={show.showId}
                    className="show-time-btn"
                    onClick={() => handleShowSelect(show, theatre)}
                  >
                    <div className="show-time">
                      {new Date(show.showTime).toLocaleTimeString('en-US', { 
                        hour: '2-digit', 
                        minute: '2-digit' 
                      })}
                    </div>
                    <div className="show-price">₹{show.price}</div>
                    <div className="show-seats">{show.availableSeats} seats</div>
                  </button>
                ))}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default MovieDetailsPage;
