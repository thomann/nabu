#!/usr/bin/perl

use DBI;

my $i = 0;
my $table = $ARGV[$i++];
my $cola = $ARGV[$i++];
my $colb = $ARGV[$i++];
my $lection = $ARGV[$i++];
my $delimiter = $ARGV[$i++];
#my $infilename

my $USER = "0";

$dbh = DBI->connect("DBI:mysql:nabu", "nabu_admin", "mypassword");

my $statement = "INSERT INTO $table (user,lection,$cola,$colb) VALUES (0,?,?,?);";
my $sth = $dbh->prepare($statement) || die $dbh->errstr;

while(<STDIN>){
    chomp;
    if(m/^#/){
        <STDIN>;
        next;
    }
     if($_ eq "nabu.voc.VocQuestionLoader"){
        next;
    }
    unless($_){
    	$lection++;
        next;
    }
    my @a = split(/$delimiter/);
    #my @b = map($dbh->quote($_), @a);
    @b = @a;
    unshift(@b, $lection);
    print "INSERT INTO $table (user,lection,$cola,$colb) VALUES (0,", join(",",@b) ,");\n";
    $sth->execute(@b) || die $dbh->errstr;
    
    #$sth->execute(map($lection,@a)) || die $dbh->errstr;
}

$dbh->disconnect() || die $dbh->errstr;
