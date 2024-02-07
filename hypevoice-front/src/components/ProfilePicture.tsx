import loadingUserIcon from "../assets/userIcon.png";
import axios from "axios";
import { useQuery } from "@tanstack/react-query";
import { MemberInfo } from "./type";

const fetchMemberInfo = async (id: string): Promise<MemberInfo> => {
  const response = await axios.get<MemberInfo>(
    `http://localhost:8080/api/members/${id}`
  );
  return response.data;
};

export default function ProfilePicture({ id }: { id: string }) {
  const { data, isLoading, isError } = useQuery<MemberInfo>({
    queryKey: ["member-info", id],
    queryFn: () => fetchMemberInfo(id),
    staleTime: 1000 * 60 * 30, // 30 minutes
  });

  if (isLoading) return <img src={loadingUserIcon} alt="Loading..." />;
  if (isError) {
    alert("프로필 사진을 불러올 수 없습니다.");
    return <img src={loadingUserIcon} alt="Loading..." />;
  }

  return (
    <div>
      <img
        src={data?.profileUrl || loadingUserIcon}
        alt={data?.nickname ? `${data.nickname}님의 프로필사진` : "프로필 사진"}
      />
    </div>
  );
}
