import React from 'react';

interface FormProps {
	createSession: () => void;
	joinSession: () => void;
	studioId: string;
	studioIdHandler: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function OpenviduForm({
	createSession,
	joinSession,
	studioId,
	studioIdHandler,
}: FormProps) {
	const onCreateHandler = () => {
		createSession();
	};
	const onJoinHandler = (event: React.FormEvent) => {
		event.preventDefault();
		joinSession();
	};

	return (
		<>
			<h1>Create a video session</h1>
			<form onSubmit={onCreateHandler}>
				<p>
					<input type="submit" value="CREATE" />
				</p>
			</form>
			<h1>Join a video session</h1>
			<form onSubmit={onJoinHandler}>
				<p>
					<input
						type="text"
						value={studioId}
						onChange={studioIdHandler}
						maxLength={8}
						required
					/>
				</p>
				<p>
					<input type="submit" value="JOIN" />
				</p>
			</form>
		</>
	);
import React from "react";
import styled from "styled-components";

const Button = styled.input`
  background-color: #5b5ff4;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  margin-top: 20px;
  margin-bottom: 40px;
  &:hover {
    background-color: #3e3e70;
  }
`;

const Input = styled.input`
  padding: 5px;
  border: 1px solid #5b5ff4;
  border-radius: 5px;
`;

const Label = styled.label`
  display: block;
  color: white;
  margin-bottom: 10px;
`;

interface FormProps {
  createSession: () => void;
  joinSession: () => void;
  studioId: string;
  studioIdHandler: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function OpenviduForm({
  createSession,
  joinSession,
  studioId,
  studioIdHandler,
}: FormProps) {
  const onCreateHandler = () => {
    createSession();
  };
  const onJoinHandler = (event: React.FormEvent) => {
    event.preventDefault();
    joinSession();
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        color: "#5b5ff4",
        backgroundColor: "#202124",
        padding: "10%",
        justifyContent: "space-around",
      }}
    >
      {/*  */}
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-around",
          alignItems: "center",
        }}
      >
        <h2 style={{ marginBottom: "20px" }}>
          HYPE VOICE에서 다른 사람들과 함께 꿈을 키워가세요!
        </h2>
        <form onSubmit={onCreateHandler}>
          <p>
            <Button type="submit" value="방 만들기" />
          </p>
        </form>
      </div>
      {/*  */}
      <form onSubmit={onJoinHandler}>
        <p>
          <Label>방 번호</Label>
          <Input
            type="text"
            value={studioId}
            onChange={studioIdHandler}
            maxLength={8}
            required
          />
        </p>
        <p>
          <Button type="submit" value="JOIN" />
        </p>
      </form>
    </div>
  );
}

export default OpenviduForm;
