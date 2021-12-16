# ElephantBook_FieldGuide

## USAGE

Upon first opening the app, you should see an list of buttons. Use the `Update API Credentials` button to
input your API credentials, update your local copy of the database using the `Update Database` button, and
then select one of the search options. Whlle searching, choosing one of the elephants from the list will
give you a screen with an elephant name, their SEEK code, a picture of the elephant, and a map of recent sitings.

## CONTRIBUTION GUIDLINES

Like every other project in GitHub, the preffered method of contributing is to fork the Repo and make changes in YourUsername/ElephantBook_FieldGuide.
If you have an old version of the ElephantBook_FieldGuide, GitHub will recognise that you are using a forked version and will give you the option to fetch upstream.
Once changes have been made, you can then open a pull request to have your changes added to the main repo. 

## Setup

These instructions will help you set up your own ElephantBook API instance.

Temporary repo for the ElephantBook project.
It requires Conda: https://www.anaconda.com/products/individual

To set up:
git clone https://github.com/Aasiya3/ElephantBook.git 
cd ElephantBook/
conda env create -f environment.yml
conda activate elephantbook
python manage.py makemigrations && python manage.py migrate

To run:
conda activate elephantbook
python manage.py runserver

Log in: Username: mep Password: mep