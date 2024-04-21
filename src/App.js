import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Main from './Main/Main.jsx';


function App() {
  return (
    <div className="App">
      <div className="App-body">
        <Routes>
          <Route path='/' element={<Main />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
