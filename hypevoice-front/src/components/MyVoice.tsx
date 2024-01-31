// My Voice SAMPLE MUI

// import React from 'react';
// import logo from './logo.svg';
// import './App.css';

import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Button from '@mui/material/Button';
import MicIcon from '@mui/icons-material/Mic';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Link from '@mui/material/Link';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import TextSnippetOutlinedIcon from '@mui/icons-material/TextSnippetOutlined';
import MovieOutlinedIcon from '@mui/icons-material/MovieOutlined';
import FavoriteIcon from '@mui/icons-material/Favorite';
import Pagination from '@mui/material/Pagination';
import Avatar from '@mui/material/Avatar';

function Copyright() {
  return (
    <Typography variant="body2" color="text.secondary" align="center">
      {'Copyright © '}
      <Link color="inherit" href="https://www.ssafy.com/">
        삼성 청년 SW 아카데미
      </Link>{' '}
      {new Date().getFullYear()}
      {/* {'.'} */}
    </Typography>
  );
}

const cards = [1, 2, 3, 4, 5, 6, 7, 8, 9];

// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

export default function App() {
  return (
    <ThemeProvider theme={defaultTheme}>
      <CssBaseline />
      <AppBar position="relative">
        <Toolbar>
          <MicIcon sx={{ mr: 2 }} />
          <Typography variant="h6" color="inherit" noWrap>
            내 보이스
          </Typography>
        </Toolbar>
      </AppBar>
      <main>
        {/* Hero unit */}
        <Box
          sx={{
            bgcolor: 'background.paper',
            pt: 8,
            pb: 6,
          }}
        >
          <Container sx={{ border: 3, borderRadius: 5, py: 8 }} maxWidth="md">
            <Grid container>
              <Grid item xs={6}>
                <Avatar
                  sx={{
                    width: 300,
                    height: 300,
                  }}
                />
              </Grid>
              <Grid item xs={6}>
                <Typography
                  component="h1"
                  variant="h2"
                  align="center"
                  color="text.primary"
                  gutterBottom
                >
                  윤선경
                </Typography>
                <Typography
                  variant="h6"
                  align="center"
                  color="text.secondary"
                  paragraph
                >
                  <p>yskkjdh@gmail.com</p>
                  <p>010-9343-6875</p>
                  <p>yskjdh@youtube.com</p>
                </Typography>
              </Grid>
            </Grid>
            {/* <Stack
              sx={{ pt: 4 }}
              direction="row"
              spacing={2}
              justifyContent="center"
            >
              <Button variant="contained">Main call to action</Button>
              <Button variant="outlined">Secondary action</Button>
            </Stack> */}
          </Container>
        </Box>

        <hr
          style={{
            width: '100%',
            textAlign: 'center',
            borderBottom: '1px solid #aaa',
            lineHeight: '0.1em',
            margin: '10px 0 50px',
          }}
        />

        <Container sx={{ border: 3, borderRadius: 5, py: 8 }} maxWidth="md">
          {/* End hero unit */}
          <Grid container spacing={4}>
            {cards.map((card) => (
              <Grid item key={card} xs={12} sm={6} md={4}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    border: 1,
                    borderRadius: 5,
                  }}
                >
                  <CardMedia
                    component="div"
                    sx={{
                      // 16:9
                      pt: '56.25%',
                    }}
                    image="https://scontent-ssn1-1.xx.fbcdn.net/v/t1.18169-9/10320375_463365673794285_2372853774846330105_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=c2f564&_nc_ohc=q81NXHajGRAAX_vDPSr&_nc_ht=scontent-ssn1-1.xx&oh=00_AfA45eBvQ677Aju9O8BqBikhYM83Afi1EULRk_7CJFiNjQ&oe=65CC2174"
                  />
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Typography gutterBottom variant="h5" component="h2">
                      [러브라이브]
                    </Typography>
                    <Typography> 안녕? 난 윤니코니코틴이야. 담타?</Typography>
                  </CardContent>
                  <CardActions>
                    <Button size="small">보기</Button>
                    <Button size="small">수정</Button>
                    <TextSnippetOutlinedIcon sx={{ mr: 2 }} />
                    <MovieOutlinedIcon sx={{ mr: 2 }} />
                  </CardActions>
                </Card>
              </Grid>
            ))}
          </Grid>
          <Pagination
            count={10}
            color="primary"
            sx={{
              align: 'center',
              mt: 3,
            }}
          />
        </Container>
      </main>
      {/* Footer */}
      <Box sx={{ bgcolor: 'background.paper', p: 6 }} component="footer">
        <Typography variant="h6" align="center" gutterBottom>
          HYPE VOICE
        </Typography>
        <Typography
          variant="subtitle1"
          align="center"
          color="text.secondary"
          component="p"
        >
          Team 백구
          <br />
          팀장 : 윤선경 / 부팀장 : 최재식 / 팀원 : 김가빈, 김금환, 김상민,
          안상준
        </Typography>
        <Copyright />
      </Box>
      {/* End footer */}
    </ThemeProvider>
  );
}
