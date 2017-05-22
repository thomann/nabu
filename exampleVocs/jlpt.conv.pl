
while(<>){
	chomp;
	next unless($_ && $_ !~ /^#/);
	my ($a, $b) = m/^([^\/]*\S?)\s*(?:\/(.*))?$/;
	my ($i, $j) = m/\s*(\S*)\s*(?:\[(.*)\])?\s*/;
	$j = $i unless($j);
	my @arr = split(/\//, $b);
	print join(":\t", @arr), "\t<--->\t$_\n";
}

