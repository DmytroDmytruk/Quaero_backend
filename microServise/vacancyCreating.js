const express = require('express');
const con = require('./connection');
const app = express();

app.use(express.json());
app.post('/creating-vacancy', (req, res) => {
  const {
    company_name,
    date_posted,
    description,
    position_title,
    salary,
    years_of_experience,
    category_id,
    employment_type_id,
    user_id,
    language_level,
    language_name,
    vacancy_id,
    name
  } = req.body;

  const sql = `INSERT INTO job_vacancies (company_name, date_posted, description, position_title, salary, years_of_experience, category_id, employment_type_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;
  const values = [company_name, date_posted, description, position_title, salary, years_of_experience, category_id, employment_type_id, user_id];

  const sql1 = `INSERT INTO job_languages (language_level, language_name, vacancy_id) VALUES (?, ?, ?)`;
  const values1 = [language_level, language_name, vacancy_id];
  
  const sql2 = `INSERT INTO job_caregories (name) VALUES (?)`;
  const values2 = [name];

  con.query(sql, values, sql1, values1, sql2, values2,  (err, result) => {
    if (err) {
      console.error('Error creating vacancies:', err);
      res.status(500).json({ message: 'Internal Server Error' });
    } else {
      res.status(201).json({ message: 'vacancies created successfully' });
    }
  });
});

module.exports = app;
