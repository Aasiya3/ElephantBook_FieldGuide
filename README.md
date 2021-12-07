# ElephantBook_FieldGuide

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