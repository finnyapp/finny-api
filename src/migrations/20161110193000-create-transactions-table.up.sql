CREATE TABLE IF NOT EXISTS transactions (
  id SERIAL PRIMARY KEY,
  value DOUBLE PRECISION CHECK (value > 0),
  comments VARCHAR(255),
  timestamp TIMESTAmp DEFAULT current_timestamp
);
