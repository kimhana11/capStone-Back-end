import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Main from './pages/Main/Main.jsx';
import Navigation from './component/Navigation/Navigation.jsx';
import Footer from './component/Footer/Footer.jsx';
import CompetitionDetail from './pages/CompetitionentDetail/CompetitionDetail.jsx';
import SignUI from './pages/SignUI/SignUI.jsx';
import MyPage from './pages/Mypage/Mypage.jsx';
import UserModify from './pages/UserModify/UserModify.jsx';
import UserProfile from './pages/UserProfile/UserProfile.jsx';


function App() {
  return (
    <div className="App">
      <Navigation/>
      <div className="App-body">
        <Routes>
          <Route path='/' element={<Main />} />
          <Route path='/signui' element={<SignUI />} />
          <Route path='/competitionDetail' element={<CompetitionDetail />} />
          <Route path='/mypage' element={<MyPage />} />
          <Route path='/userModify' element={<UserModify />} />
          <Route path='/userProfile' element={<UserProfile />} />
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
