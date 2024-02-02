import React, { useState } from "react";
import { Link } from "react-router-dom";
import {
  List,
  ListItem,
  Button,
  TextField,
  ButtonGroup,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import Pagination from "@mui/material/Pagination";
import posts from "./PostsDummyData";
import styled from "styled-components";

// typescript 타입
export interface Post {
  category: string;
  id: number;
  title: string;
  author: string;
  date: string;
  viewcnt: number;
}

const CommunityStyleDiv = styled.div`
  .community-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    /* width: 95%; */
    /* margin: 100px auto; */
  }

  .community-header {
    display: flex;
    justify-content: space-between;
    width: 100%;
    /* margin-bottom: 2rem; */
    /* border-bottom: 2px solid rgba(0, 0, 0, 0.1); */
  }

  .community-header-left {
  }

  .community-header-right {
  }

  .community-post-header {
    display: flex;
    align-items: center;
    padding: 1rem 0;
    /* margin-bottom: 1rem; */
    font-size: 120%;
    border-bottom: 2px solid rgba(0, 0, 0, 0.1);
  }

  .community-post-header div {
    width: 25%;
    display: inline-block;
    text-align: center;
  }

  .community-post-header-id {
    flex-basis: 8%;
  }

  .community-post-header-category {
    flex-basis: 8%;
  }

  .community-post-header-title {
    flex-basis: 52%;
  }

  .community-post-header-title a {
    text-decoration: none;
    transition: color 0.3s ease;
  }

  .community-post-header-title a:hover {
  }

  .community-post-header-author {
    flex-basis: 16%;
  }

  .community-post-header-viewcnt {
    flex-basis: 8%;
  }

  .community-post-header-date {
    flex-basis: 8%;
  }

  .pagination {
    margin-top: 1rem;
    display: flex;
    justify-content: center;
  }

  .community-post-container {
    width: 100%;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    border-radius: 4px;
    padding: 1rem;
  }

  .community-post {
    display: flex;
    align-items: center;
    padding: 1rem 0;
    margin-bottom: 0.3rem;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  }

  .community-post div {
    width: 25%;
    display: inline-block;
    text-align: center;
  }

  .community-post-id {
    flex-basis: 8%;
    /* font-weight: bold; */
  }

  .community-post-category {
    flex-basis: 8%;
  }

  .community-post-title {
    flex-basis: 52%;
  }

  .community-post-title a {
    text-decoration: none;
    color: #333333;
    transition: color 0.3s ease;
  }

  .community-post-title a:hover {
    color: #007bff;
  }

  .community-post-author {
    flex-basis: 16%;
  }

  .community-post-viewcnt {
    flex-basis: 8%;
  }

  .community-post-date {
    flex-basis: 8%;
  }

  .pagination {
    margin-top: 1rem;
    display: flex;
    justify-content: center;
  }

  /* .search-bar {
		margin-top: 1rem;
		display: flex;
		justify-content: center;
	}

	.search-bar input[type='text'] {
		padding: 0.5rem;
		border-radius: 4px;
	}

	.search-bar button {
		margin-left: 0.5rem;
		padding: 0.5rem;
		border-radius: 4px;
		border: none;
		background-color: #007bff;
		color: white;
		cursor: pointer;
	}

	.search-bar button:hover {
		background-color: #0056b3;
	} */
`;

const CommunityComponent = () => {
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [currentcategory, setCurrentcategory] = useState<string>("전체");
  const [searchKeyword, setSearchKeyword] = useState<string>("");
  const [postsPerPage, setPostsPerPage] = useState<number>(10);

  const filteredPosts: Post[] =
    currentcategory === "전체"
      ? posts
      : posts.filter((post) => post.category === currentcategory);

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

  const handleButtonClick = (category: string): void => {
    setCurrentcategory(category);
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
    <CommunityStyleDiv>
      <div className="community-container">
        <div className="community-header">
          <div className="community-header-left">
            <ButtonGroup
              color="primary"
              aria-label="category buttons"
              sx={{ m: 1 }}
            >
              <Button
                onClick={() => handleButtonClick("전체")}
                variant={currentcategory === "전체" ? "contained" : "outlined"}
              >
                전체
              </Button>
              <Button
                onClick={() => handleButtonClick("자유")}
                variant={currentcategory === "자유" ? "contained" : "outlined"}
              >
                자유
              </Button>
              <Button
                onClick={() => handleButtonClick("피드백")}
                variant={
                  currentcategory === "피드백" ? "contained" : "outlined"
                }
              >
                피드백
              </Button>
              <Button
                onClick={() => handleButtonClick("구인구직")}
                variant={
                  currentcategory === "구인구직" ? "contained" : "outlined"
                }
              >
                구인구직
              </Button>
            </ButtonGroup>
          </div>
          <div className="community-header-right">
            <Button variant="contained" sx={{ m: 1 }}>
              글쓰기
            </Button>
            <FormControl variant="standard" sx={{ m: 1, minWidth: 90 }}>
              <InputLabel id="demo-simple-select-standard-label">
                페이지 글 개수
              </InputLabel>
              <Select
                name="selectPostsPerPage"
                value={postsPerPage}
                onChange={(e) => {
                  setPostsPerPage(Number(e.target.value));
                  setCurrentPage(1);
                }}
              >
                <MenuItem value={10}>10개</MenuItem>
                <MenuItem value={20}>20개</MenuItem>
                <MenuItem value={30}>30개</MenuItem>
              </Select>
            </FormControl>
          </div>
        </div>
        <List className="community-post-container">
          <ListItem className="community-post-header">
            <div className="community-post-header-id">번호</div>
            <div className="community-post-header-category">분류</div>
            <div className="community-post-header-title">제목</div>
            <div className="community-post-header-author">작성자</div>
            <div className="community-post-header-date">작성일자</div>
            <div className="community-post-header-viewcnt">조회수</div>
          </ListItem>
          {currentPosts.map((content) => (
            <ListItem key={content.id} className="community-post">
              <div className="community-post-id">{content.id}</div>
              <div className="community-post-category">{content.category}</div>
              <div className="community-post-title">
                <Link to={`/boards/${content.id}`} className="board-link">
                  <span className="board-title">{content.title}</span>
                </Link>
              </div>
              <div className="community-post-author">{content.author}</div>
              <div className="community-post-date">{content.date}</div>
              <div className="community-post-viewcnt">{content.viewcnt}</div>
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
            id="standard-basic"
            label="검색어"
            onChange={handleSearchChange}
            variant="standard"
            sx={{ m: 1 }}
          />
        </div>
      </div>
    </CommunityStyleDiv>
  );
};

export default CommunityComponent;
