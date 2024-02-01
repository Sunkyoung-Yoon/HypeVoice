import React, { useState } from "react";
import { Link } from "react-router-dom";
import { List, ListItem, Button, TextField, ButtonGroup } from "@mui/material";
import Pagination from "@mui/material/Pagination";
// import "./Community.css";
// import posts from "./PostsDummyData";

// typescript 타입
export interface Post {
  classification: string;
  id: number;
  title: string;
  author: string;
  date: string;
  viewcnt: number;
}

const Community = () => {
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [currentClassification, setCurrentClassification] =
    useState<string>("전체");
  const [searchKeyword, setSearchKeyword] = useState<string>("");
  const [postsPerPage, setPostsPerPage] = useState<number>(10);

  const filteredPosts: Post[] =
    currentClassification === "전체"
      ? posts
      : posts.filter((post) => post.classification === currentClassification);

  const searchedPosts: Post[] = searchKeyword
    ? filteredPosts.filter(
        (post) =>
          post.title.includes(searchKeyword) ||
          post.author.includes(searchKeyword)
      )
    : filteredPosts;

  const pageCount: number = Math.ceil(searchedPosts.length / postsPerPage);

  const handlePageClick = (
    event: React.ChangeEvent<unknown>,
    pageNumber: number
  ): void => {
    setCurrentPage(pageNumber);
  };

  const handleButtonClick = (classification: string): void => {
    setCurrentClassification(classification);
    setCurrentPage(1);
  };

  const handleSearchChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ): void => {
    setSearchKeyword(event.target.value);
  };

  const indexOfLastPost: number = currentPage * postsPerPage;
  const indexOfFirstPost: number = indexOfLastPost - postsPerPage;
  const currentPosts: Post[] = searchedPosts.slice(
    indexOfFirstPost,
    indexOfLastPost
  );

  return (
    <div className="board-list-container">
      <div className="header">
        <ButtonGroup
          color="primary"
          aria-label="classification buttons"
          sx={{ m: 1 }}
        >
          <Button
            onClick={() => handleButtonClick("전체")}
            variant={
              currentClassification === "전체" ? "contained" : "outlined"
            }
          >
            전체
          </Button>
          <Button
            onClick={() => handleButtonClick("자유")}
            variant={
              currentClassification === "자유" ? "contained" : "outlined"
            }
          >
            자유
          </Button>
          <Button
            onClick={() => handleButtonClick("피드백")}
            variant={
              currentClassification === "피드백" ? "contained" : "outlined"
            }
          >
            피드백
          </Button>
          <Button
            onClick={() => handleButtonClick("구인구직")}
            variant={
              currentClassification === "구인구직" ? "contained" : "outlined"
            }
          >
            구인구직
          </Button>
        </ButtonGroup>
        <Button variant="contained" sx={{ m: 1 }}>
          <Link to={`/write`}>
            <span>글쓰기</span>
          </Link>
        </Button>
        <select
          name="selectPostsPerPage"
          value={postsPerPage}
          onChange={(e) => {
            setPostsPerPage(Number(e.target.value));
            setCurrentPage(1);
          }}
        >
          <option value={10}>10</option>
          <option value={20}>20</option>
          <option value={30}>30</option>
        </select>
        d d
      </div>
      <List className="board-list">
        <ListItem className="board-list-item">
          <div className="board-list-item-id">번호</div>
          <div className="board-list-item-classification">분류</div>
          <div className="board-list-item-title">제목</div>
          <div className="board-list-item-author">작성자</div>
          <div className="board-list-item-date">작성일자</div>
          <div className="board-list-item-viewcnt">조회수</div>
        </ListItem>
        {currentPosts.map((content) => (
          <ListItem key={content.id} className="board-list-item">
            <div className="board-list-item-id">{content.id}</div>
            <div className="board-list-item-classification">
              {content.classification}
            </div>
            <div className="board-list-item-title">
              <Link to={`/boards/${content.id}`} className="board-link">
                <span className="board-title">{content.title}</span>
              </Link>
            </div>
            <div className="board-list-item-author">{content.author}</div>
            <div className="board-list-item-date">{content.date}</div>
            <div className="board-list-item-viewcnt">{content.viewcnt}</div>
          </ListItem>
        ))}
      </List>
      <div className="pagination">
        <Pagination
          count={pageCount}
          page={currentPage}
          onChange={handlePageClick}
        />
      </div>
      <div className="search-bar">
        <TextField
          label="검색어"
          value={searchKeyword}
          onChange={handleSearchChange}
          sx={{ m: 1, width: "100%" }}
        />
      </div>
      <div></div>
    </div>
  );
};

export default Community;
