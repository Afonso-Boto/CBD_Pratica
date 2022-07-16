from random import randrange

def insert_user(username, name, email, register_time):
    return f"INSERT INTO user(username, name, email, register_time) VALUES(\'{username}\', \'{name}\', \'{email}\', \'{register_time}\');\n"

def insert_video(author, title, description, tags, upload_timestamp):
    return f"INSERT INTO video(author, title, description, tags, upload_timestamp) VALUES(\'{author}\', \'{title}\', \'{description}\', {tags}, \'{upload_timestamp}\');\n"

def insert_comment(video, user, content, time):
    return f"INSERT INTO comment(video, user, content, time) VALUES(\'{video}\', \'{user}\', \'{content}\', \'{time}\');\n"

def insert_follower(video, username):
    return f"INSERT INTO follower (video, username) VALUES(\'{video}\', \'{username}\');\n"

def insert_events(username, video ,event, date, duration):
    return f"INSERT INTO event (username, video, event, date, duration) VALUES( \'{username}\', \'{video}\', \'{event}\', \'{date}\', {duration});\n"

def insert_ratings(rating_id, video, rating):
    return f"INSERT INTO rating (rating_id, video, rating) VALUES(now(), \'{video}\', {rating});\n"


with open("alinea_b.cql", "w") as f:
    autores = [a + " " + b for a in ["afonso", "ricardo", "jacinto", "gabriel", "luis", "pedro", "andre"] for b in ["silva", "pereira", "cardoso", "boto"]]
    tags = ["comedia", "drama", "acão", "terror", "gameplay", "vlog", "politica", "Aveiro"]
    tipos = ["play", "pause", "stop"]
    

    #user 10
    for i in range(10):
        author = autores[randrange(len(autores))]
        print(type("123"))
        f.write(insert_user(author, author + "123", autores[randrange(len(autores))] + "@gmail.com", f"2021-12-1T12:{i}"))

    f.write("\n")

    # videos 10
    for i in range(10):
        f.write(insert_video(autores[randrange(len(autores))], f"video{i}", "description", {tags[randrange(len(tags))], tags[randrange(len(tags))]}, f"2021-12-1T12:{i}"))
    
    f.write("\n")

    # comentarios 20
    for i in range(20):
        f.write(insert_comment(f"video{randrange(1, 10)}", autores[randrange(len(autores))] + "123", "comentario", f"2021-12-1T12:{i}"))
    
    f.write("\n")

    # followers 20
    for i in range(10):
        f.write(insert_follower(f"video{randrange(1, 10)}", autores[randrange(len(autores))]))
    
    f.write("\n")
    # eventos 10
    for i in range(10):
        f.write(insert_events(autores[randrange(len(autores))] + "123",f"video{randrange(1, 10)}", tipos[randrange(len(tipos))], f"2021-12-1T12:{i}", randrange(1000)))

    f.write("\n")
    # rating 10
    for i in range(10):
        f.write(insert_ratings(i, f"video{randrange(1, 10)}", randrange(6)))