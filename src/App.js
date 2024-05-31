import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Main from './pages/Main/Main.jsx';
import Navigation from './component/Navigation/Navigation.jsx';
import CompetitionDetail from './pages/CompetitionentDetail/CompetitionDetail.jsx';
import SignUI from './pages/SignUI/SignUI.jsx';
import MyPage from './pages/Mypage/Mypage.jsx';
import Cheer from './component/Cheer/Cheer.jsx';


function App() {
  return (
    <div className="App">
      <Cheer/>
      <Navigation/>
      <div className="App-body">
        <Routes>
          <Route path='/' element={<Main />} />
          <Route path='/signui' element={<SignUI />} />
          <Route path='/competitionDetail' element={<CompetitionDetail />} />
          <Route path='/mypage' element={<MyPage />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
