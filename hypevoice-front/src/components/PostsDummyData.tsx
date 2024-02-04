interface Post {
  category: string;
  id: number;
  title: string;
  author: string;
  date: string;
  viewcnt: number;
}

function createData(
  category: string,
  id: number,
  title: string,
  author: string,
  date: string,
  viewcnt: number
): Post {
  return { category, id, title, author, date, viewcnt };
}

const posts: Post[] = [
  createData("피드백", 24, "제목24", "작성자24", "20240130", 10),
  createData("자유", 23, "제목23", "작성자23", "20240129", 22),
  createData("구인구직", 22, "제목22", "작성자22", "20240128", 8),
  createData("피드백", 21, "제목21", "작성자21", "20240131", 0),
  createData("피드백", 20, "제목20", "작성자20", "20240130", 10),
  createData("자유", 19, "제목19", "작성자19", "20240129", 22),
  createData("구인구직", 18, "제목18", "작성자18", "20240128", 8),
  createData("피드백", 17, "제목17", "작성자17", "20240131", 0),
  createData("피드백", 16, "제목16", "작성자16", "20240130", 10),
  createData("자유", 15, "제목15", "작성자15", "20240129", 22),
  createData("구인구직", 14, "제목14", "작성자14", "20240128", 8),
  createData("피드백", 13, "제목13", "작성자13", "20240131", 0),
  createData("피드백", 12, "제목12", "작성자12", "20240130", 10),
  createData("자유", 11, "제목11", "작성자11", "20240129", 22),
  createData("구인구직", 10, "제목10", "작성자10", "20240128", 8),
  createData("피드백", 9, "제목9", "작성자9", "20240131", 0),
  createData("피드백", 8, "제목8", "작성자8", "20240130", 10),
  createData("자유", 7, "제목7", "작성자7", "20240129", 22),
  createData("구인구직", 6, "제목6", "작성자6", "20240128", 8),
  createData("피드백", 5, "제목5", "작성자5", "20240131", 0),
  createData("피드백", 4, "제목4", "작성자4", "20240130", 10),
  createData("자유", 3, "제목3", "작성자3", "20240129", 22),
  createData("구인구직", 2, "제목2", "작성자2", "20240128", 8),
  createData("피드백", 1, "제목1", "작성자1", "20240131", 0),
];

export default posts;
